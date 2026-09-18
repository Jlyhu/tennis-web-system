package com.fsteni58.tennis.dto;

import java.util.UUID;

public class ProductoResponse {
    private UUID id;
    private String mensaje;

    public ProductoResponse(UUID id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    public UUID getId() { return id; }
    public String getMensaje() { return mensaje; }
}