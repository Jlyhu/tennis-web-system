/*
 * Programa:     CamposInvalidosException.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Excepción no verificada (RuntimeException) que se lanza
 *               cuando los datos recibidos no cumplen las reglas de
 *               validación del negocio. Transporta un mensaje que describe
 *               el problema para que pueda informarse a quien hizo la
 *               petición.
 */
package com.fsteni58.tennis.exception;

public class CamposInvalidosException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje descripción de los campos inválidos o faltantes
     */
    public CamposInvalidosException(String mensaje) {
        super(mensaje);
    }
}