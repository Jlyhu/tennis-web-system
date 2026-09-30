/*
 * Programa:     ResponderTicketRequest.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada para responder un ticket de soporte.
 *               Transporta el texto de la respuesta del administrador
 *               desde el body de la petición
 *               PUT /admin/tickets/{id}/responder. La restricción de
 *               validación (respuesta obligatoria) se aplica con @Valid en
 *               TicketSoporteController antes de llegar a la capa de
 *               servicio.
 */
package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.NotBlank;

public class ResponderTicketRequest {

    /** Texto de la respuesta del administrador. Obligatorio, no puede estar vacío. */
    @NotBlank(message = "La respuesta no puede estar vacía")
    private String respuesta;

    /**
     * Obtiene el texto de la respuesta.
     * @return respuesta del administrador
     */
    public String getRespuesta() { return respuesta; }

    /**
     * Asigna el texto de la respuesta.
     * @param respuesta respuesta del administrador
     */
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
}