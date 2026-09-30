/*

Programa:     TicketSoporte.java
Versión:      1.0
Fecha:        30/09/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Clase modelo que representa la entidad TicketSoporte en el
              sistema. Almacena la información referente a las consultas, 
              dudas o reclamos generados por un comprador, así como la 
              respuesta del equipo de soporte, fechas relevantes y su estado.
*/

package com.fsteni58.tennis.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Modelo de dominio que representa un ticket de soporte.
 * Permite gestionar y hacer seguimiento a las incidencias reportadas 
 * por los usuarios compradores, controlando su ciclo de vida a través 
 * de diferentes estados (abierto, respondido, cerrado).
 */
public class TicketSoporte {
    private UUID id;
    private UUID compradorId;
    private String asunto;
    private String descripcion;
    private String respuesta;
    private String estado; // abierto, respondido, cerrado
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime fechaRespuesta;

    /**
     * Constructor por defecto.
     * Requerido habitualmente para procesos de serialización/deserialización 
     * o por frameworks de mapeo de datos.
     */
    public TicketSoporte() {}

    /**
     * Obtiene el identificador único del ticket de soporte.
     * 
     * @return id del ticket
     */
    public UUID getId() { return id; }

    /**
     * Establece el identificador único del ticket de soporte.
     * 
     * @param id nuevo identificador UUID del ticket
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Obtiene el identificador del comprador que creó el ticket.
     * 
     * @return id del comprador asociado
     */
    public UUID getCompradorId() { return compradorId; }

    /**
     * Establece el identificador del comprador que genera el ticket.
     * 
     * @param compradorId UUID del comprador
     */
    public void setCompradorId(UUID compradorId) { this.compradorId = compradorId; }

    /**
     * Obtiene el asunto o título principal del ticket.
     * 
     * @return asunto del ticket
     */
    public String getAsunto() { return asunto; }

    /**
     * Establece el asunto o motivo principal del ticket.
     * 
     * @param asunto texto breve que describe el motivo de contacto
     */
    public void setAsunto(String asunto) { this.asunto = asunto; }

    /**
     * Obtiene la descripción detallada del problema o consulta.
     * 
     * @return descripción del ticket
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Establece la descripción detallada del problema o consulta.
     * 
     * @param descripcion cuerpo completo del mensaje del comprador
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * Obtiene la respuesta proporcionada por el equipo de soporte.
     * 
     * @return texto con la respuesta de soporte, o null si aún no hay respuesta
     */
    public String getRespuesta() { return respuesta; }

    /**
     * Establece la respuesta proporcionada por el equipo de soporte.
     * 
     * @param respuesta mensaje de resolución o aclaración para el comprador
     */
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    /**
     * Obtiene el estado actual del ticket.
     * 
     * @return estado del ticket (ej. "abierto", "respondido", "cerrado")
     */
    public String getEstado() { return estado; }

    /**
     * Establece el estado del ticket en el flujo de atención.
     * 
     * @param estado nuevo estado a asignar al ticket
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene la fecha y hora exactas en que se creó el ticket.
     * 
     * @return fecha y hora de creación (incluyendo zona horaria)
     */
    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }

    /**
     * Establece la fecha y hora en que se generó el ticket.
     * 
     * @param fechaCreacion fecha y hora de creación
     */
    public void setFechaCreacion(OffsetDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    /**
     * Obtiene la fecha y hora en que se brindó respuesta al ticket.
     * 
     * @return fecha y hora de respuesta (incluyendo zona horaria), o null si no se ha respondido
     */
    public OffsetDateTime getFechaRespuesta() { return fechaRespuesta; }

    /**
     * Establece la fecha y hora de la respuesta proporcionada por soporte.
     * 
     * @param fechaRespuesta fecha y hora exacta de la respuesta
     */
    public void setFechaRespuesta(OffsetDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
}