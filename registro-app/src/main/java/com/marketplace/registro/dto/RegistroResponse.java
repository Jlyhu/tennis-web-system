package com.marketplace.registro.dto;

import java.util.UUID;

public class RegistroResponse {

    private UUID id;
    private String nombre;
    private String correo;
    private String rol;
    private String mensaje;

    public RegistroResponse(UUID id, String nombre, String correo, String rol, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }

    public String getMensaje() {
        return mensaje;
    }
}
