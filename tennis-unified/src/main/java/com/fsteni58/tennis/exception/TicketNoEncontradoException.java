/*

Programa:     TicketNoEncontradoException.java
Versión:      1.0
Fecha:        30/09/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Excepción personalizada (RuntimeException) que se lanza 
              cuando se intenta consultar, actualizar o realizar alguna 
              operación sobre un ticket de soporte que no existe en 
              la base de datos.
*/

package com.fsteni58.tennis.exception;

/**
 * Excepción que indica que un ticket de soporte solicitado no fue encontrado.
 * Extiende de RuntimeException para permitir su manejo global (por ejemplo,
 * en un GlobalExceptionHandler) sin necesidad de declararla en las firmas de los métodos.
 */
public class TicketNoEncontradoException extends RuntimeException {

    /**
     * Construye una nueva excepción con el mensaje de error especificado.
     * 
     * @param mensaje el mensaje que detalla el motivo de la excepción (ej. "Ticket con ID X no encontrado")
     */
    public TicketNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}