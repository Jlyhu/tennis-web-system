package com.fsteni58.tennis.dto;

public class MensajeResponse {
    private String mensaje;

    public MensajeResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() { return mensaje; }
}