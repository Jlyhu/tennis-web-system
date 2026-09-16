package com.marketplace.registro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Correo repetido -> 409 Conflict
    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> manejarCorreoDuplicado(CorreoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    // Campos faltantes o mal formados detectados en el service -> 400 Bad Request
    @ExceptionHandler(CamposInvalidosException.class)
    public ResponseEntity<Map<String, String>> manejarCamposInvalidos(CamposInvalidosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Campos faltantes detectados por las anotaciones @NotBlank/@Email del DTO -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacionDto(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Datos inválidos");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", mensaje));
    }

    // Cualquier otro error inesperado -> 500, sin exponer detalles internos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Ocurrió un error inesperado. Intenta de nuevo más tarde."));
    }

}
