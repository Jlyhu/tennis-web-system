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

    // Correo repetido -> 409 Conflict
    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<MensajeResponse> manejarCorreoDuplicado(CorreoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensajeResponse(ex.getMessage()));
    }

    // Campos faltantes o mal formados detectados a mano en el service -> 400 Bad Request
    @ExceptionHandler(CamposInvalidosException.class)
    public ResponseEntity<MensajeResponse> manejarCamposInvalidos(CamposInvalidosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensajeResponse(ex.getMessage()));
    }

    // Otras validaciones de negocio (proveedor inválido, precio negativo, etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensajeResponse> manejarIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensajeResponse(ex.getMessage()));
    }

    // Campos faltantes detectados por @NotBlank/@Email del DTO vía @Valid -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacionDto(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // Cualquier otro error inesperado -> 500, sin exponer detalles internos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeResponse> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeResponse("Ocurrió un error inesperado. Intenta de nuevo más tarde."));
    }

    // Login fallido (usuario no existe o contraseña incorrecta) -> 401 Unauthorized
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<MensajeResponse> manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MensajeResponse(ex.getMessage()));
    }

    @ExceptionHandler(TicketNoEncontradoException.class)
        public ResponseEntity<?> manejarTicketNoEncontrado(TicketNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}