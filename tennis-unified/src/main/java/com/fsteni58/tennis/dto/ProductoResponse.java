/*
 * Programa:     ProductoResponse.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida tras registrar un producto. Devuelve el id
 *               generado del producto y un mensaje de confirmación. Es
 *               inmutable: sus valores se asignan solo por constructor.
 */
package com.fsteni58.tennis.dto;

import java.util.UUID;

public class ProductoResponse {

    /** Identificador único (UUID) generado para el producto. */
    private UUID id;

    /** Mensaje de confirmación de la operación. */
    private String mensaje;

    /**
     * Crea la respuesta del registro de un producto.
     * @param id      identificador único (UUID) generado para el producto
     * @param mensaje mensaje de confirmación
     */
    public ProductoResponse(UUID id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el identificador del producto.
     * @return id del producto
     */
    public UUID getId() { return id; }

    /**
     * Obtiene el mensaje de la respuesta.
     * @return mensaje de confirmación
     */
    public String getMensaje() { return mensaje; }
}