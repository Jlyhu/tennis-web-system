package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/** Cuerpo de POST /inventario/movimientos. En entrada y salida la cantidad es positiva; en ajuste lleva signo. */
public record MovimientoRequest(
        @NotNull(message = "El producto es obligatorio") UUID productoId,
        @NotBlank(message = "El tipo es obligatorio")
        @Pattern(regexp = "entrada|salida|ajuste", message = "El tipo debe ser entrada, salida o ajuste") String tipo,
        @NotNull(message = "La cantidad es obligatoria") Integer cantidad,
        @Size(max = 150, message = "El motivo no puede superar 150 caracteres") String motivo) {}