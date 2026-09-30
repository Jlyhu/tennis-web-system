/*
 * Programa:     ItemPedidoDTO.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO (record) que representa un ítem dentro de una orden de
 *               pedido: el producto, la cantidad solicitada y su precio
 *               unitario. Es inmutable y calcula el subtotal del ítem a
 *               partir de esos valores.
 */
package com.fsteni58.tennis.dto;

import java.math.BigDecimal;

/**
 * @param idProducto     identificador del producto
 * @param nombreProducto nombre del producto
 * @param cantidad       cantidad de unidades del producto en el pedido
 * @param precioUnitario precio de una unidad del producto
 */
public record ItemPedidoDTO(
    String idProducto,
    String nombreProducto,
    int cantidad,
    BigDecimal precioUnitario
) {

    /**
     * Calcula el subtotal del ítem (precio unitario x cantidad).
     * @return subtotal del ítem
     */
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}