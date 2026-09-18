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

    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<?> manejarCorreoDuplicado(CorreoDuplicadoException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<?> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<?> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> manejarArgumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
