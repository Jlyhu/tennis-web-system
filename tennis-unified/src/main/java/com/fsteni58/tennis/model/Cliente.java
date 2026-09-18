package com.fsteni58.tennis.model;

import java.util.UUID;

public class Cliente {
    private UUID id;
    private String nombre;
    private String correo;
    private boolean activo;

    public Cliente() {}

    public Cliente(UUID id, String nombre, String correo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
