/*
 * Programa:     ClienteResponse.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida con los datos de un cliente. Lo construye
 *               ClienteController a partir de la entidad Cliente para
 *               responder a las consultas de /admin/clientes sin exponer
 *               el modelo directamente. Es inmutable: sus valores se
 *               asignan solo por constructor.
 */
package com.fsteni58.tennis.dto;

import java.util.UUID;

public class ClienteResponse {

    /** Identificador único (UUID) del cliente. */
    private UUID id;

    /** Nombre del cliente. */
    private String nombre;

    /** Correo del cliente. */
    private String correo;

    /** Indica si el cliente está activo (false si fue desactivado). */
    private boolean activo;

    /**
     * Crea la respuesta con los datos de un cliente.
     * @param id     identificador único (UUID) del cliente
     * @param nombre nombre del cliente
     * @param correo correo del cliente
     * @param activo true si el cliente está activo, false si fue desactivado
     */
    public ClienteResponse(UUID id, String nombre, String correo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    /**
     * Obtiene el identificador del cliente.
     * @return id del cliente
     */
    public UUID getId() { return id; }

    /**
     * Obtiene el nombre del cliente.
     * @return nombre del cliente
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el correo del cliente.
     * @return correo del cliente
     */
    public String getCorreo() { return correo; }

    /**
     * Indica si el cliente está activo.
     * @return true si el cliente está activo, false si fue desactivado
     */
    public boolean isActivo() { return activo; }
}