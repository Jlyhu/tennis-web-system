package com.fsteni58.tennis.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;
import java.util.Map;

/**
 * Reglas de seguridad de la API: qué rol puede entrar a cada ruta.
 * Los endpoints nuevos que no aparezcan aquí exigen, como mínimo, sesión iniciada.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http, SecurityContextRepository repositorio) throws Exception {
        http
            // La sesión va en una cookie SameSite=Lax: el navegador no la envía en POST/PUT/PATCH/DELETE
            // que vengan de otro sitio, así que no se usa token CSRF.
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .securityContext(sc -> sc.securityContextRepository(repositorio))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/imagenes/**", "/favicon.png", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/productos", "/productos/*").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/productos").hasRole("PROVEEDOR")
                .requestMatchers(HttpMethod.PATCH, "/productos/*/stock").hasRole("PROVEEDOR")
                .requestMatchers("/inventario/**").hasRole("PROVEEDOR")
                .requestMatchers("/carrito/**").hasRole("COMPRADOR")
                .requestMatchers("/tickets/**").hasRole("COMPRADOR")
                .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/estado").hasAnyRole("ADMINISTRADOR", "PROVEEDOR")
                .requestMatchers("/api/pedidos/**").authenticated()
                .requestMatchers("/api/compras/**").hasRole("COMPRADOR")
.requestMatchers("/api/promociones/**").hasRole("COMPRADOR")
                .anyRequest().authenticated())
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> responder(res, 401, "Inicia sesión para continuar."))
                .accessDeniedHandler((req, res, ex) -> responder(res, 403, "No tienes permiso para esta acción.")))
            .logout(l -> l
                .logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK)));
        return http.build();
    }

    /** Respuesta de error con el mismo formato del resto de la API: {"mensaje": "..."}. */
    private void responder(HttpServletResponse res, int estado, String mensaje) throws IOException {
        res.setStatus(estado);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("mensaje", mensaje)));
    }
}