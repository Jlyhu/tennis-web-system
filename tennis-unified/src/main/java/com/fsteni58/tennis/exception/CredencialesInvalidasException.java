/*
 * Programa:     CredencialesInvalidasException.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Excepción no verificada (RuntimeException) que se lanza
 *               cuando el correo o la contraseña enviados en el inicio de
 *               sesión (Comprador US2) no son correctos. Transporta un
 *               mensaje que describe el problema para que pueda
 *               informarse a quien hizo la petición.
 */
package com.fsteni58.tennis.exception;

public class CredencialesInvalidasException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje descripción del fallo de autenticación
     */
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}