/*
 * Programa:     ActualizarClienteRequest.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada para actualizar los datos de un cliente.
 *               Transporta el nuevo nombre y correo desde el body de la
 *               petición PUT /admin/clientes/{id}. Las restricciones de
 *               validación (campos obligatorios y formato de correo) se
 *               aplican con @Valid en ClienteController antes de llegar a
 *               la capa de servicio.
 */
package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ActualizarClienteRequest {

    /** Nuevo nombre del cliente. Obligatorio, no puede estar vacío. */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /** Nuevo correo del cliente. Obligatorio y con formato de correo válido. */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    /**
     * Obtiene el nombre del cliente.
     * @return nombre del cliente
     */
    public String getNombre() { return nombre; }

    /**
     * Asigna el nombre del cliente.
     * @param nombre nuevo nombre del cliente
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el correo del cliente.
     * @return correo del cliente
     */
    public String getCorreo() { return correo; }

    /**
     * Asigna el correo del cliente.
     * @param correo nuevo correo del cliente
     */
    public void setCorreo(String correo) { this.correo = correo; }
}