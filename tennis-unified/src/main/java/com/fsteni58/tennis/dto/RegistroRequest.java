/*
 * Programa:     RegistroRequest.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada para el registro de usuarios, comprador o
 *               proveedor (Comprador US1). Transporta los datos desde el
 *               body de la petición POST /auth/register. Las restricciones
 *               de validación de los campos comunes se aplican con @Valid
 *               en AuthController; los campos exclusivos de proveedor
 *               (nombreEmpresa, nit, telefono, direccion) se validan en
 *               AuthService.registrar() cuando el rol es "proveedor".
 */
package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroRequest {

    /** Nombre del usuario. Obligatorio, no puede estar vacío. */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /** Correo del usuario. Obligatorio y con formato de correo válido. */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    /** Contraseña del usuario. Obligatoria, con mínimo 8 caracteres. */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;

    /** Rol del usuario (comprador o proveedor). Obligatorio, no puede estar vacío. */
    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    // Campos exclusivos de proveedor. Opcionales a nivel de anotación porque
    // un comprador nunca los envía; AuthService valida que vengan diligenciados
    // cuando rol = "proveedor".

    /** Nombre de la empresa del proveedor. Solo para rol proveedor. */
    private String nombreEmpresa;

    /** NIT de la empresa del proveedor. Solo para rol proveedor. */
    private String nit;

    /** Teléfono de contacto del proveedor. Solo para rol proveedor. */
    private String telefono;

    /** Dirección del proveedor. Solo para rol proveedor. */
    private String direccion;

    /**
     * Obtiene el nombre del usuario.
     * @return nombre del usuario
     */
    public String getNombre() { return nombre; }

    /**
     * Asigna el nombre del usuario.
     * @param nombre nombre del usuario
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

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

    /**
     * Obtiene el rol del usuario.
     * @return rol del usuario (comprador o proveedor)
     */
    public String getRol() { return rol; }

    /**
     * Asigna el rol del usuario.
     * @param rol rol del usuario (comprador o proveedor)
     */
    public void setRol(String rol) { this.rol = rol; }

    /**
     * Obtiene el nombre de la empresa.
     * @return nombre de la empresa (solo proveedor; puede ser null)
     */
    public String getNombreEmpresa() { return nombreEmpresa; }

    /**
     * Asigna el nombre de la empresa.
     * @param nombreEmpresa nombre de la empresa del proveedor
     */
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    /**
     * Obtiene el NIT de la empresa.
     * @return NIT de la empresa (solo proveedor; puede ser null)
     */
    public String getNit() { return nit; }

    /**
     * Asigna el NIT de la empresa.
     * @param nit NIT de la empresa del proveedor
     */
    public void setNit(String nit) { this.nit = nit; }

    /**
     * Obtiene el teléfono del proveedor.
     * @return teléfono del proveedor (solo proveedor; puede ser null)
     */
    public String getTelefono() { return telefono; }

    /**
     * Asigna el teléfono del proveedor.
     * @param telefono teléfono de contacto del proveedor
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Obtiene la dirección del proveedor.
     * @return dirección del proveedor (solo proveedor; puede ser null)
     */
    public String getDireccion() { return direccion; }

    /**
     * Asigna la dirección del proveedor.
     * @param direccion dirección del proveedor
     */
    public void setDireccion(String direccion) { this.direccion = direccion; }
}