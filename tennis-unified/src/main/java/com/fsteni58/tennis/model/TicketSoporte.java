package com.fsteni58.tennis.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TicketSoporte {
    private UUID id;
    private UUID compradorId;
    private String asunto;
    private String descripcion;
    private String respuesta;
    private String estado; // abierto, respondido, cerrado
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime fechaRespuesta;

    public TicketSoporte() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCompradorId() { return compradorId; }
    public void setCompradorId(UUID compradorId) { this.compradorId = compradorId; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(OffsetDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public OffsetDateTime getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(OffsetDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
}
