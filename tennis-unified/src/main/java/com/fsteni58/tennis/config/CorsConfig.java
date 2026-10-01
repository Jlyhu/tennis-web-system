/*

Programa:     CorsConfig.java
Versión:      1.0
Fecha:        01/10/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Clase de configuración global de Spring Boot que define 
              las políticas de CORS (Cross-Origin Resource Sharing). 
              Permite la comunicación entre el cliente web (frontend, 
              como tu aplicación en Vue.js) y los endpoints de esta API 
              REST, evitando bloqueos de seguridad por parte del navegador.
*/

package com.fsteni58.tennis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para la aplicación.
 * Implementa WebMvcConfigurer para personalizar las configuraciones 
 * web por defecto de Spring MVC, habilitando el acceso a los recursos 
 * desde dominios externos.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configura los mapeos y reglas de CORS para la API.
     * En esta implementación, se permite el acceso a todas las rutas ("/**"),
     * desde cualquier origen o dominio ("*") y habilitando los métodos HTTP 
     * fundamentales para realizar las operaciones CRUD (GET, POST, PUT, DELETE).
     * 
     * @param registry el registro de CORS proporcionado por Spring para 
     *                 añadir las reglas de mapeo correspondientes
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}