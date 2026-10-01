/*

Programa:     PasswordEncoderConfig.java
Versión:      1.0
Fecha:        01/10/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Clase de configuración de Spring que define el Bean del 
              codificador de contraseñas. Utiliza el algoritmo BCrypt 
              para encriptar las credenciales de los usuarios, garantizando 
              que nunca se guarden ni se comparen en texto plano, 
              aumentando así la seguridad del sistema.
*/

package com.fsteni58.tennis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad orientada a la encriptación de contraseñas.
 * Provee el componente centralizado (Bean) que será inyectado en la capa de 
 * servicios (como AuthService) para procesar el registro y el inicio de sesión.
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Instancia y expone el codificador de contraseñas para el contexto de Spring.
     * Implementa BCrypt, un algoritmo de hash seguro que incorpora un "salt" 
     * aleatorio por defecto, protegiendo las contraseñas contra ataques de 
     * diccionario o tablas rainbow.
     * 
     * @return una instancia de BCryptPasswordEncoder lista para ser utilizada en la aplicación
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}