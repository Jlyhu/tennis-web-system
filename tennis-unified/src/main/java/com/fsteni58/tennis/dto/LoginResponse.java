package com.fsteni58.tennis.dto;

import java.util.UUID;

public class LoginResponse {
    private UUID id;
    private String nombre;
    private String mensaje;

    public LoginResponse(UUID id, String nombre, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getMensaje() { return mensaje; }
}