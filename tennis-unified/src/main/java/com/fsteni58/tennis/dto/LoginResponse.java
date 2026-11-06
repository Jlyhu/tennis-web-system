/*
 * Programa:     LoginResponse.java
 * Versión:      1.1
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida del inicio de sesión (Comprador US2). Lo
 *               construye AuthController tras autenticar al usuario en
 *               POST /auth/login y devuelve su id, su nombre, su rol, el
 *               id de proveedor (solo si es proveedor) y un mensaje de
 *               confirmación. Es inmutable: sus valores se asignan solo
 *               por constructor.
 * Cambios v1.1: se agregaron rol y proveedorId para que el front lleve al
 *               usuario a su panel según el rol.
 */
package com.fsteni58.tennis.dto;

import java.util.UUID;

public class LoginResponse {

    /** Identificador único (UUID) del usuario autenticado. */
    private UUID id;

    /** Nombre del usuario autenticado. */
    private String nombre;

    /** Nombre del rol del usuario (comprador, proveedor o administrador). */
    private String rol;

    /** Identificador del proveedor, o null si el usuario no es proveedor. */
    private UUID proveedorId;

    /** Mensaje de confirmación del inicio de sesión. */
    private String mensaje;

    /**
     * Crea la respuesta de un inicio de sesión exitoso.
     * @param id          identificador único (UUID) del usuario autenticado
     * @param nombre      nombre del usuario autenticado
     * @param rol         nombre del rol del usuario
     * @param proveedorId id del proveedor (null si no es proveedor)
     * @param mensaje     mensaje de confirmación
     */
    public LoginResponse(UUID id, String nombre, String rol, UUID proveedorId, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.proveedorId = proveedorId;
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el identificador del usuario.
     * @return id del usuario autenticado
     */
    public UUID getId() { return id; }

    /**
     * Obtiene el nombre del usuario.
     * @return nombre del usuario autenticado
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el rol del usuario.
     * @return nombre del rol
     */
    public String getRol() { return rol; }

    /**
     * Obtiene el id de proveedor del usuario.
     * @return id del proveedor, o null si el usuario no es proveedor
     */
    public UUID getProveedorId() { return proveedorId; }

    /**
     * Obtiene el mensaje de la respuesta.
     * @return mensaje de confirmación
     */
    public String getMensaje() { return mensaje; }
}