package com.fsteni58.tennis.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IdentidadConfig implements WebMvcConfigurer {

    private final ControlDeIdentidad controlDeIdentidad;
private final UsuarioIdDesdeSesion usuarioIdDesdeSesion;

public IdentidadConfig(ControlDeIdentidad controlDeIdentidad, UsuarioIdDesdeSesion usuarioIdDesdeSesion) {
    this.controlDeIdentidad = controlDeIdentidad;
    this.usuarioIdDesdeSesion = usuarioIdDesdeSesion;
}

@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(controlDeIdentidad)
            .addPathPatterns("/carrito/**", "/tickets/**", "/inventario/**", "/productos/**");
    registry.addInterceptor(usuarioIdDesdeSesion)
            .addPathPatterns("/api/compras/**", "/api/promociones/**");
}
}