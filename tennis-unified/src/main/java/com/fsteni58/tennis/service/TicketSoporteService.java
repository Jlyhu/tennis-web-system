/*
 * Programa:     TicketSoporteService.java
 * Versión:      1.0
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de negocio del módulo de tickets de soporte
 *               (Comprador US8, generar PQRS; Administrador US10, canales
 *               de comunicación). Ver TicketSoporteServiceTest.java para
 *               las pruebas unitarias (PU-001, PU-002, PU-003).
 */
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.exception.TicketNoEncontradoException;
import com.fsteni58.tennis.model.TicketSoporte;
import com.fsteni58.tennis.repository.TicketSoporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Lógica de negocio del módulo de tickets de soporte (Comprador US8,
 * generar PQRS; Administrador US10, canales de comunicación).
 * Ver TicketSoporteServiceTest para las pruebas unitarias (PU-001, PU-002, PU-003).
 *
 * @author Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *         Moreno Cortes, Orosco Quemba
 * @version 1.0
 * @since 29/09/2026
 */
@Service
public class TicketSoporteService {

    private final TicketSoporteRepository ticketRepository;

    public TicketSoporteService(TicketSoporteRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Crea un ticket de soporte nuevo (Comprador US8).
     * Nota: este método no valida sus parámetros por sí mismo (ver PU-001);
     * la validación real ocurre en CrearTicketRequest vía @NotNull/@NotBlank,
     * antes de que el controlador llame a este método.
     * @param compradorId id del comprador que reporta el problema
     * @param asunto asunto breve del reporte
     * @param descripcion detalle del problema o consulta
     * @return id generado para el nuevo ticket
     */
    public UUID crearTicket(UUID compradorId, String asunto, String descripcion) {
        return ticketRepository.crear(compradorId, asunto, descripcion);
    }

    /**
     * Retorna todos los tickets del sistema (vista del Administrador).
     * @return lista completa de tickets
     */
    public List<TicketSoporte> listarTodos() {
        return ticketRepository.listarTodos();
    }

    /**
     * Retorna únicamente los tickets creados por un comprador específico.
     * @param compradorId id del comprador
     * @return lista de tickets de ese comprador
     */
    public List<TicketSoporte> listarPorComprador(UUID compradorId) {
        return ticketRepository.listarPorComprador(compradorId);
    }

    /**
     * Busca un ticket por su id.
     * @param id id del ticket
     * @return el ticket encontrado
     * @throws TicketNoEncontradoException si no existe un ticket con ese id
     */
    public TicketSoporte buscarPorId(UUID id) {
        return ticketRepository.buscarPorId(id)
                .orElseThrow(() -> new TicketNoEncontradoException("No existe un ticket con id " + id));
    }

    /**
     * Registra la respuesta del administrador a un ticket (Administrador US10).
     * Defecto conocido (ver Test Case TC-002, Canales de Comunicación): este
     * método no valida el estado del ticket antes de responder, por lo que
     * responder un ticket cerrado lo reabre.
     * @param id id del ticket a responder
     * @param respuesta texto de la respuesta del administrador
     * @throws TicketNoEncontradoException si no existe un ticket con ese id
     */
    public void responder(UUID id, String respuesta) {
        buscarPorId(id); // confirma que existe
        ticketRepository.responder(id, respuesta);
    }

    /**
     * Cambia el estado de un ticket a cerrado (Administrador US10).
     * Defecto conocido (ver Test Case TC-002, Canales de Comunicación): este
     * método no valida que el ticket haya sido respondido antes, por lo que
     * se puede cerrar un ticket que nunca tuvo respuesta.
     * @param id id del ticket a cerrar
     * @throws TicketNoEncontradoException si no existe un ticket con ese id
     */
    public void cerrar(UUID id) {
        buscarPorId(id);
        ticketRepository.cerrar(id);
    }
}