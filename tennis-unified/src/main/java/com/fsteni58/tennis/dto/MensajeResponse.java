/*
 * Programa:     MensajeResponse.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida genérico que transporta únicamente un
 *               mensaje de texto. Lo usan los controladores (por ejemplo,
 *               ClienteController) para confirmar operaciones que no
 *               devuelven datos, como actualizar o desactivar. Es
 *               inmutable: su valor se asigna solo por constructor.
 */
package com.fsteni58.tennis.dto;

public class MensajeResponse {

    /** Mensaje de texto de la respuesta. */
    private String mensaje;

    /**
     * Crea una respuesta con un mensaje.
     * @param mensaje mensaje de texto a devolver
     */
    public MensajeResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el mensaje de la respuesta.
     * @return mensaje de texto
     */
    public String getMensaje() { return mensaje; }
}