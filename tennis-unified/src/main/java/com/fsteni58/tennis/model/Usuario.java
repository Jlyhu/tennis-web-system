/*

Programa:     Usuario.java
Versión:      1.0
Fecha:        30/09/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Clase modelo que representa la entidad Usuario en el
              sistema. Contiene la información principal para el control
              de acceso (autenticación y autorización), incluyendo sus 
              credenciales encriptadas, datos de contacto y el rol 
              asignado dentro de la plataforma (comprador, proveedor, etc.).
*/

package com.fsteni58.tennis.model;

import java.util.UUID;

/**
 * Modelo de dominio que representa a un usuario registrado en el sistema.
 * Es la entidad central para el manejo de sesiones, validación de credenciales
 * y gestión de perfiles (roles) dentro de la tienda.
 */
public class Usuario {
    private UUID id;
    private String nombre;
    private String correo;
    private String contrasenaHash;
    private UUID rolId;
    private boolean activo;

    /**
     * Constructor por defecto.
     * Requerido habitualmente para procesos de instanciación dinámica, 
     * serialización/deserialización o frameworks de persistencia.
     */
    public Usuario() {}

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return id del usuario
     */
    public UUID getId() { return id; }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id nuevo identificador UUID
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Obtiene el nombre completo o alias del usuario.
     * 
     * @return nombre del usuario
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre completo o alias del usuario.
     * 
     * @param nombre nuevo nombre a asignar
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el correo electrónico del usuario, utilizado como 
     * identificador para el inicio de sesión.
     * 
     * @return correo electrónico
     */
    public String getCorreo() { return correo; }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param correo nueva dirección de correo electrónico
     */
    public void setCorreo(String correo) { this.correo = correo; }

    /**
     * Obtiene la contraseña del usuario en formato encriptado (hash).
     * Por seguridad, nunca se debe almacenar la contraseña en texto plano.
     * 
     * @return hash de la contraseña
     */
    public String getContrasenaHash() { return contrasenaHash; }

    /**
     * Establece la contraseña del usuario en formato encriptado.
     * 
     * @param contrasenaHash nueva contraseña previamente hasheada
     */
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    /**
     * Obtiene el identificador del rol asignado a este usuario.
     * Permite definir los permisos y accesos dentro del sistema.
     * 
     * @return id del rol del usuario
     */
    public UUID getRolId() { return rolId; }

    /**
     * Establece el identificador del rol para este usuario.
     * 
     * @param rolId UUID del rol (ej. ID correspondiente a "Comprador" o "Proveedor")
     */
    public void setRolId(UUID rolId) { this.rolId = rolId; }

    /**
     * Verifica si el usuario se encuentra activo y tiene permitido
     * el ingreso a la plataforma.
     * 
     * @return true si el usuario está activo, false si está bloqueado o inactivo
     */
    public boolean isActivo() { return activo; }

    /**
     * Establece el estado de actividad de la cuenta del usuario.
     * 
     * @param activo true para habilitar el acceso del usuario, false para deshabilitarlo
     */
    public void setActivo(boolean activo) { this.activo = activo; }
}