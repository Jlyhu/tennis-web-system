package com.fsteni58.tennis.model;

import java.util.UUID;

// Representa una fila de la tabla "usuarios" en Supabase
public class Usuario {
    private UUID id;
    private String nombre;
    private String correo;
    private String contrasenaHash; // nunca guardamos la contraseña real, solo su hash
    private UUID rolId;
    private boolean activo;

    public Usuario() {}

    public Usuario(UUID id, String nombre, String correo, String contrasenaHash, UUID rolId, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
        this.rolId = rolId;
        this.activo = activo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public UUID getRolId() { return rolId; }
    public void setRolId(UUID rolId) { this.rolId = rolId; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
