
package com.fsteni58.tennis.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record CompraResponse(
    UUID id,
    OffsetDateTime fecha,
    String estado,
    BigDecimal total,
    List<ItemCompraResponse> productos
) {
    public record ItemCompraResponse(
        UUID productoId,
        String nombreProducto,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
    ) {}
}
