/*
 * Programa:     RegistroResponse.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida del registro de usuarios (Comprador US1). Lo
 *               construye AuthController tras registrar al usuario en
 *               POST /auth/register y devuelve el id generado y un mensaje
 *               de confirmación. Es inmutable: sus valores se asignan solo
 *               por constructor.
 */
package com.fsteni58.tennis.dto;

import java.util.UUID;

public class RegistroResponse {

    /** Identificador único (UUID) generado para el usuario registrado. */
    private UUID id;

    /** Mensaje de confirmación del registro. */
    private String mensaje;

    /**
     * Crea la respuesta de un registro exitoso.
     * @param id      identificador único (UUID) generado para el usuario
     * @param mensaje mensaje de confirmación
     */
    public RegistroResponse(UUID id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el identificador del usuario registrado.
     * @return id del usuario
     */
    public UUID getId() { return id; }

    /**
     * Obtiene el mensaje de la respuesta.
     * @return mensaje de confirmación
     */
    public String getMensaje() { return mensaje; }
}