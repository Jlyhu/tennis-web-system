/*
 * Programa:     ActualizarStockRequest.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada para la actualización de stock (Proveedor
 *               US5). Las anotaciones de Jakarta Validation son las que
 *               producen los mensajes "El stock nuevo es obligatorio" y
 *               "El stock no puede ser negativo" documentados en el
 *               Test Case TC-001 (Actualizar Stock).
 */
package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ActualizarStockRequest {

    /**
     * Nuevo valor de stock del producto. Debe venir informado (no nulo) y
     * ser mayor o igual a 0.
     */
    @NotNull(message = "El stock nuevo es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}