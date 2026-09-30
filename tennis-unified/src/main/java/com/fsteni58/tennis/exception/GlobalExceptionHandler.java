/*
 * Programa:     GlobalExceptionHandler.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Manejador global de excepciones de la API REST
 *               (@RestControllerAdvice). Intercepta las excepciones que
 *               lanzan los controladores y servicios y las traduce a
 *               respuestas HTTP con el código de estado correspondiente:
 *               409 (correo duplicado), 400 (campos o argumentos
 *               inválidos), 401 (credenciales inválidas), 404 (ticket no
 *               encontrado) y 500 (cualquier error inesperado, sin exponer
 *               detalles internos).
 */
package com.fsteni58.tennis.exception;

import com.fsteni58.tennis.dto.MensajeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja el intento de registrar un correo que ya existe.
     * @param ex excepción lanzada por el servicio
     * @return 409 Conflict con el mensaje de la excepción
     */
    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<MensajeResponse> manejarCorreoDuplicado(CorreoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensajeResponse(ex.getMessage()));
    }

    /**
     * Maneja campos faltantes o mal formados detectados a mano en el
     * servicio (por ejemplo, datos obligatorios de proveedor).
     * @param ex excepción lanzada por el servicio
     * @return 400 Bad Request con el mensaje de la excepción
     */
    @ExceptionHandler(CamposInvalidosException.class)
    public ResponseEntity<MensajeResponse> manejarCamposInvalidos(CamposInvalidosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensajeResponse(ex.getMessage()));
    }

    /**
     * Maneja otras validaciones de negocio que lanzan IllegalArgumentException
     * (proveedor inválido, precio negativo, etc.).
     * @param ex excepción lanzada por el servicio
     * @return 400 Bad Request con el mensaje de la excepción
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensajeResponse> manejarIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensajeResponse(ex.getMessage()));
    }

    /**
     * Maneja los errores de validación de los DTO detectados por las
     * anotaciones (@NotBlank, @Email, etc.) al usar @Valid.
     * @param ex excepción con el detalle de los campos que fallaron
     * @return 400 Bad Request con un mapa campo -> mensaje de error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacionDto(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    /**
     * Maneja cualquier otro error inesperado sin exponer detalles internos.
     * @param ex excepción no contemplada por los demás manejadores
     * @return 500 Internal Server Error con un mensaje genérico
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeResponse> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeResponse("Ocurrió un error inesperado. Intenta de nuevo más tarde."));
    }

    /**
     * Maneja el inicio de sesión fallido (el usuario no existe o la
     * contraseña es incorrecta).
     * @param ex excepción lanzada por el servicio
     * @return 401 Unauthorized con el mensaje de la excepción
     */
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<MensajeResponse> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MensajeResponse(ex.getMessage()));
    }

    /**
     * Maneja la consulta de un ticket de soporte que no existe.
     * @param ex excepción lanzada por el servicio
     * @return 404 Not Found con un mapa que contiene el mensaje bajo la clave "error"
     */
    @ExceptionHandler(TicketNoEncontradoException.class)
        public ResponseEntity<?> manejarTicketNoEncontrado(TicketNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}