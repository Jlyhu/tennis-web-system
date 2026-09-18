package com.fsteni58.tennis.dto;

import java.util.UUID;

public class ClienteResponse {
    private UUID id;
    private String nombre;
    private String correo;
    private boolean activo;

    public ClienteResponse(UUID id, String nombre, String correo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public boolean isActivo() { return activo; }
}