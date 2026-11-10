package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

/** Cuerpo de POST /admin/liquidaciones. motivoSalidaId solo aplica (y es obligatorio) si el tipo es "final". */
public record LiquidacionRequest(
        @NotNull(message = "El proveedor es obligatorio") UUID proveedorId,
        @NotNull(message = "La fecha de inicio es obligatoria") LocalDate fechaInicio,
        @NotNull(message = "La fecha de fin es obligatoria") LocalDate fechaFin,
        @NotBlank(message = "El tipo es obligatorio")
        @Pattern(regexp = "periodica|final", message = "El tipo debe ser periodica o final") String tipo,
        UUID motivoSalidaId) {}