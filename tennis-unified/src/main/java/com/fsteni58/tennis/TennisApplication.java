/*

Programa:     TennisApplication.java
Versión:      1.0
Fecha:        01/10/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Clase principal que inicializa y arranca la aplicación 
              Spring Boot. Se encarga de cargar las variables de entorno 
              desde un archivo .env local y establecerlas en el sistema 
              antes de levantar el contexto completo de Spring.
*/

package com.fsteni58.tennis;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal (Entry point) de la aplicación de la tienda virtual de tenis.
 * Configura e inicia el framework Spring Boot y prepara el entorno de ejecución.
 */
@SpringBootApplication
public class TennisApplication {

    /**
     * Método principal que arranca la aplicación.
     * Primero intenta cargar un archivo .env (omitiendo errores si no existe en producción) 
     * y transfiere esas variables al entorno del sistema (System Properties). 
     * Esto permite que Spring Boot pueda utilizar credenciales y configuraciones seguras 
     * (por ejemplo, conexión a Supabase/Render) de manera transparente.
     * Finalmente, levanta el contexto de Spring.
     * 
     * @param args argumentos de línea de comandos pasados al iniciar la aplicación
     */
    public static void main(String[] args) {
        // Carga variables del archivo .env y las pasa a System.properties
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue()));

        // Inicia la aplicación Spring Boot
        SpringApplication.run(TennisApplication.class, args);
    }
}