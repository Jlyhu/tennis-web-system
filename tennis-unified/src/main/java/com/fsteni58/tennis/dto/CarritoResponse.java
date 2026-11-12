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

import com.fsteni58.tennis.util.Tarifas;
import java.math.BigDecimal;
import java.util.List;

public class CarritoResponse {

    private List<CarritoItemResponse> items;
    private BigDecimal total;          // subtotal, sin IVA
    private BigDecimal iva;
    private BigDecimal totalConIva;
    private int cantidadTotal;

    public CarritoResponse(List<CarritoItemResponse> items) {
        this.items = items;
        this.total = items.stream()
                .map(CarritoItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.iva = Tarifas.ivaDe(this.total);
        this.totalConIva = this.total.add(this.iva);
        this.cantidadTotal = items.stream()
                .mapToInt(CarritoItemResponse::getCantidad)
                .sum();
    }

    public List<CarritoItemResponse> getItems() { return items; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getIva() { return iva; }
    public BigDecimal getTotalConIva() { return totalConIva; }
    public int getCantidadTotal() { return cantidadTotal; }
}