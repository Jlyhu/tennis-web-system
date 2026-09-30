/*
 * Programa:     PedidoException.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Excepción no verificada (RuntimeException) que se lanza
 *               cuando una operación sobre un pedido no puede realizarse
 *               (por ejemplo, al crear una orden o cambiar su estado).
 *               Transporta un mensaje que describe el problema para que
 *               pueda informarse a quien hizo la petición.
 */
package com.fsteni58.tennis.exception;

public class PedidoException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     * @param mensaje descripción del problema con el pedido
     */
    public PedidoException(String mensaje) {
        super(mensaje);
    }
}