package com.fsteni58.tennis.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
    String idProducto,
    String nombreProducto,
    int cantidad,
    BigDecimal precioUnitario
) {
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}