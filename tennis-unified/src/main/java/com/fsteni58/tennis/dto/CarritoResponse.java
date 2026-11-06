/*
 * Programa:     CarritoResponse.java
 * Versión:      1.0
 * Fecha:        06/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida con el carrito completo de un comprador:
 *               la lista de ítems, el total a pagar y el número total de
 *               unidades. El total y la cantidad se calculan al construirlo.
 */
package com.fsteni58.tennis.dto;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponse {

    /** Productos que hay en el carrito (vacía si no hay ninguno). */
    private List<CarritoItemResponse> items;

    /** Suma de los subtotales, en COP. */
    private BigDecimal total;

    /** Suma de las cantidades de todos los ítems. */
    private int cantidadTotal;

    /**
     * Crea la respuesta del carrito y calcula el total y la cantidad total.
     * @param items ítems del carrito
     */
    public CarritoResponse(List<CarritoItemResponse> items) {
        this.items = items;
        this.total = items.stream()
                .map(CarritoItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.cantidadTotal = items.stream()
                .mapToInt(CarritoItemResponse::getCantidad)
                .sum();
    }

    /**
     * Obtiene los ítems del carrito.
     * @return lista de ítems
     */
    public List<CarritoItemResponse> getItems() { return items; }

    /**
     * Obtiene el total a pagar.
     * @return total en COP
     */
    public BigDecimal getTotal() { return total; }

    /**
     * Obtiene el número total de unidades.
     * @return suma de las cantidades
     */
    public int getCantidadTotal() { return cantidadTotal; }
}