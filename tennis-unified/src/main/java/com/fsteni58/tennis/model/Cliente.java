/*

Programa:     Cliente.java
Versión:      1.0
Fecha:        30/09/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Clase modelo que representa la entidad Cliente en el
              sistema. Contiene la información básica del cliente como  
              su identificador único (UUID), nombre, correo electrónico 
              y su estado actual en la plataforma (activo/inactivo).  
*/

package com.fsteni58.tennis.model;

import java.util.UUID;

/**
 * Modelo de dominio que representa a un cliente.
 * Almacena los datos personales y el estado de la cuenta para su uso 
 * en las distintas capas de la aplicación.
 */
public class Cliente {
    private UUID id;
    private String nombre;
    private String correo;
    private boolean activo;

    /**
     * Constructor por defecto.
     * Requerido habitualmente para procesos de serialización/deserialización (ej. JSON)
     * o por frameworks de persistencia.
     */
    public Cliente() {}

    /**
     * Constructor con parámetros para inicializar todos los atributos del cliente.
     * 
     * @param id     identificador único del cliente
     * @param nombre nombre completo del cliente
     * @param correo dirección de correo electrónico de contacto
     * @param activo estado de la cuenta (true si está activa, false en caso contrario)
     */
    public Cliente(UUID id, String nombre, String correo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    /**
     * Obtiene el identificador único del cliente.
     * 
     * @return id del cliente
     */
    public UUID getId() { return id; }

    /**
     * Establece el identificador único del cliente.
     * 
     * @param id nuevo identificador UUID
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Obtiene el nombre completo del cliente.
     * 
     * @return nombre del cliente
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre completo del cliente.
     * 
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el correo electrónico del cliente.
     * 
     * @return correo electrónico
     */
    public String getCorreo() { return correo; }

    /**
     * Establece el correo electrónico del cliente.
     * 
     * @param correo nueva dirección de correo
     */
    public void setCorreo(String correo) { this.correo = correo; }

    /**
     * Verifica si el cliente se encuentra activo en el sistema.
     * 
     * @return true si el cliente está activo, false si está inactivo
     */
    public boolean isActivo() { return activo; }

    /**
     * Establece el estado de actividad del cliente.
     * 
     * @param activo true para marcar al cliente como activo, false para inactivarlo
     */
    public void setActivo(boolean activo) { this.activo = activo; }
}