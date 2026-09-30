/*
 * Programa:     LoginRequest.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada para el inicio de sesión (Comprador US2).
 *               Transporta el correo y la contraseña desde el body de la
 *               petición POST /auth/login. Las restricciones de validación
 *               (campos obligatorios y formato de correo) se aplican con
 *               @Valid en AuthController antes de llegar a la capa de
 *               servicio.
 */
package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    /** Correo del usuario. Obligatorio y con formato de correo válido. */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    /** Contraseña del usuario. Obligatoria, no puede estar vacía. */
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    /**
     * Obtiene el correo del usuario.
     * @return correo del usuario
     */
    public String getCorreo() { return correo; }

    /**
     * Asigna el correo del usuario.
     * @param correo correo del usuario
     */
    public void setCorreo(String correo) { this.correo = correo; }

    /**
     * Obtiene la contraseña del usuario.
     * @return contraseña del usuario
     */
    public String getContrasena() { return contrasena; }

    /**
     * Asigna la contraseña del usuario.
     * @param contrasena contraseña del usuario
     */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}