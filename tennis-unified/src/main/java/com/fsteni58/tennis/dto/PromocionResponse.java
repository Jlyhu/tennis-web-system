
package com.fsteni58.tennis.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PromocionResponse(
    UUID id,
    String nombre,
    String descripcion,
    String tipoDescuento,
    BigDecimal valor,
    OffsetDateTime fechaInicio,
    OffsetDateTime fechaFin,
    UUID productoId,
    String nombreProducto,
    String categoria,
    BigDecimal precioOriginal,
    BigDecimal precioFinal,
    String motivo
) {}
