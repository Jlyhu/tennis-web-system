/*
 * Programa:     CorreoDuplicadoException.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Excepción no verificada (RuntimeException) que se lanza
 *               cuando se intenta usar un correo que ya está registrado
 *               por otro usuario. Transporta un mensaje que describe el
 *               problema para que pueda informarse a quien hizo la
 *               petición.
 */
package com.fsteni58.tennis.exception;

public class CorreoDuplicadoException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje descripción del correo que ya está registrado
     */
    public CorreoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}