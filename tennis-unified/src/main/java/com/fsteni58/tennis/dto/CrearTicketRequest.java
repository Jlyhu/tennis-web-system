/*
 * Programa:     CrearTicketRequest.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada para crear un ticket de soporte. Transporta
 *               el comprador que reporta el problema, el asunto y la
 *               descripción desde el body de la petición POST /tickets.
 *               Las restricciones de validación (campos obligatorios) se
 *               aplican con @Valid en TicketSoporteController antes de
 *               llegar a la capa de servicio.
 */
package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CrearTicketRequest {

    /** Identificador único (UUID) del comprador que reporta el problema. Obligatorio. */
    @NotNull(message = "El id del comprador es obligatorio")
    private UUID compradorId;

    /** Asunto o título breve del ticket. Obligatorio, no puede estar vacío. */
    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    /** Descripción detallada del problema. Obligatoria, no puede estar vacía. */
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    /**
     * Obtiene el identificador del comprador.
     * @return id del comprador que crea el ticket
     */
    public UUID getCompradorId() { return compradorId; }

    /**
     * Asigna el identificador del comprador.
     * @param compradorId id del comprador que crea el ticket
     */
    public void setCompradorId(UUID compradorId) { this.compradorId = compradorId; }

    /**
     * Obtiene el asunto del ticket.
     * @return asunto del ticket
     */
    public String getAsunto() { return asunto; }

    /**
     * Asigna el asunto del ticket.
     * @param asunto asunto del ticket
     */
    public void setAsunto(String asunto) { this.asunto = asunto; }

    /**
     * Obtiene la descripción del problema.
     * @return descripción del problema reportado
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Asigna la descripción del problema.
     * @param descripcion descripción del problema reportado
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}