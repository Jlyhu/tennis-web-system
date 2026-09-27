package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.NotBlank;

public class ResponderTicketRequest {

    @NotBlank(message = "La respuesta no puede estar vacía")
    private String respuesta;

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
}
