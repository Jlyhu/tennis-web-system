/*
 * Programa:     OrdenPedidoResponse.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida (record) con los datos de una orden de
 *               pedido: identificador, cliente, fecha, estado, ítems y
 *               total. Lo devuelve PedidoController en las operaciones de
 *               /api/pedidos. Es inmutable: sus valores se asignan solo al
 *               construirlo.
 */
package com.fsteni58.tennis.dto;

import com.fsteni58.tennis.model.EstadoOrden;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @param id      identificador único (UUID) de la orden
 * @param cliente cliente que realizó el pedido
 * @param fecha   fecha de la orden
 * @param estado  estado actual de la orden
 * @param items   lista de ítems (producto, cantidad y precio unitario) de la orden
 * @param total   valor total de la orden
 */
public record OrdenPedidoResponse(
    UUID id,
    String cliente,
    LocalDate fecha,
    EstadoOrden estado,
    List<ItemPedidoDTO> items,
    BigDecimal total
) {}