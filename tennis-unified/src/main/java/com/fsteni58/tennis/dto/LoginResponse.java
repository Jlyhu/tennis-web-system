/*
 * Programa:     LoginResponse.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida del inicio de sesión (Comprador US2). Lo
 *               construye AuthController tras autenticar al usuario en
 *               POST /auth/login y devuelve su id, su nombre y un mensaje
 *               de confirmación. Es inmutable: sus valores se asignan solo
 *               por constructor.
 */
package com.fsteni58.tennis.dto;

import java.util.UUID;

public class LoginResponse {

    /** Identificador único (UUID) del usuario autenticado. */
    private UUID id;

    /** Nombre del usuario autenticado. */
    private String nombre;

    /** Mensaje de confirmación del inicio de sesión. */
    private String mensaje;

    /**
     * Crea la respuesta de un inicio de sesión exitoso.
     * @param id      identificador único (UUID) del usuario autenticado
     * @param nombre  nombre del usuario autenticado
     * @param mensaje mensaje de confirmación
     */
    public LoginResponse(UUID id, String nombre, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el identificador del usuario.
     * @return id del usuario autenticado
     */
    public UUID getId() { return id; }

    /**
     * Obtiene el nombre del usuario.
     * @return nombre del usuario autenticado
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el mensaje de la respuesta.
     * @return mensaje de confirmación
     */
    public String getMensaje() { return mensaje; }
}