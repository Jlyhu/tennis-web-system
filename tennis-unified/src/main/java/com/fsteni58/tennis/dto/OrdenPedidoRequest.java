/*
 * Programa:     OrdenPedidoRequest.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada (record) para crear una orden de pedido.
 *               Transporta el cliente y la lista de ítems desde el body de
 *               la petición POST /api/pedidos. Es inmutable y no declara
 *               restricciones de validación: los datos se procesan y
 *               validan en PedidoService.
 */
package com.fsteni58.tennis.dto;

import java.util.List;

/**
 * @param cliente cliente que realiza el pedido
 * @param items   lista de ítems (producto, cantidad y precio unitario) del pedido
 */
public record OrdenPedidoRequest(
    String cliente,
    List<ItemPedidoDTO> items
) {}