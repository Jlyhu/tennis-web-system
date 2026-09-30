/*
 * Programa:     ManejadorExcepciones.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Manejador de excepciones de la API REST
 *               (@RestControllerAdvice). Centraliza la conversión de las
 *               excepciones de negocio en respuestas JSON con formato
 *               {"error": mensaje}, en vez de repetir try/catch en cada
 *               controlador: 400 (correo duplicado y argumentos
 *               inválidos), 401 (credenciales inválidas) y 404 (cliente no
 *               encontrado).
 */
package com.fsteni58.tennis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Un solo lugar para convertir cada excepción en una respuesta JSON clara,
// en vez de repetir try/catch en cada controlador.
@RestControllerAdvice
public class ManejadorExcepciones {

    /**
     * Maneja el intento de registrar un correo que ya existe.
     * @param ex excepción lanzada por el servicio
     * @return 400 Bad Request con un mapa que contiene el mensaje bajo la clave "error"
     */
    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<?> manejarCorreoDuplicado(CorreoDuplicadoException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    /**
     * Maneja el inicio de sesión fallido (el usuario no existe o la
     * contraseña es incorrecta).
     * @param ex excepción lanzada por el servicio
     * @return 401 Unauthorized con un mapa que contiene el mensaje bajo la clave "error"
     */
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<?> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }

    /**
     * Maneja la consulta de un cliente que no existe.
     * @param ex excepción lanzada por el servicio
     * @return 404 Not Found con un mapa que contiene el mensaje bajo la clave "error"
     */
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<?> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    /**
     * Maneja las validaciones de negocio que lanzan IllegalArgumentException
     * (argumentos o datos inválidos).
     * @param ex excepción lanzada por el servicio
     * @return 400 Bad Request con un mapa que contiene el mensaje bajo la clave "error"
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> manejarArgumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}