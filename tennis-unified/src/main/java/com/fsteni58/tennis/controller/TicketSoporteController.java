/*
 * Programa:     TicketSoporteController.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Controlador REST de tickets de soporte. Permite al
 *               comprador reportar un problema y consultar sus propios
 *               tickets (/tickets), y al administrador listar todos los
 *               tickets, consultar uno, responderlo y cerrarlo
 *               (/admin/tickets). Recibe las peticiones HTTP y delega toda
 *               la lógica de negocio a TicketSoporteService.
 */
package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.CrearTicketRequest;
import com.fsteni58.tennis.dto.ResponderTicketRequest;
import com.fsteni58.tennis.model.TicketSoporte;
import com.fsteni58.tennis.service.TicketSoporteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class TicketSoporteController {

    private final TicketSoporteService ticketService;

    public TicketSoporteController(TicketSoporteService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Crea un ticket de soporte a nombre de un comprador que reporta un
     * problema. La validación de los campos del body la aplica @Valid sobre
     * CrearTicketRequest antes de que este método se ejecute.
     * @param request datos del ticket (compradorId, asunto y descripción)
     * @return 200 OK con el id generado del ticket y un mensaje de confirmación
     */
    @PostMapping("/tickets")
    public ResponseEntity<?> crear(@Valid @RequestBody CrearTicketRequest request) {
        UUID idGenerado = ticketService.crearTicket(
                request.getCompradorId(), request.getAsunto(), request.getDescripcion());
        return ResponseEntity.ok(Map.of("id", idGenerado, "mensaje", "Ticket creado correctamente"));
    }

    /**
     * Lista los tickets creados por un comprador (vista del comprador).
     * @param compradorId identificador único (UUID) del comprador, enviado como parámetro de consulta
     * @return lista de tickets del comprador
     */
    @GetMapping("/tickets/mios")
    public List<TicketSoporte> misTickets(@RequestParam UUID compradorId) {
        return ticketService.listarPorComprador(compradorId);
    }

    /**
     * Lista todos los tickets de soporte (vista del administrador).
     * @return lista con todos los tickets
     */
    @GetMapping("/admin/tickets")
    public List<TicketSoporte> listarTodos() {
        return ticketService.listarTodos();
    }

    /**
     * Consulta un ticket de soporte por su identificador (vista del administrador).
     * @param id identificador único (UUID) del ticket, tomado de la URL
     * @return el ticket encontrado
     */
    @GetMapping("/admin/tickets/{id}")
    public TicketSoporte verUno(@PathVariable UUID id) {
        return ticketService.buscarPorId(id);
    }

    /**
     * Registra la respuesta del administrador a un ticket. La validación
     * del body la aplica @Valid sobre ResponderTicketRequest antes de que
     * este método se ejecute.
     * @param id      identificador único (UUID) del ticket, tomado de la URL
     * @param request contiene el texto de la respuesta
     * @return 200 OK con un mensaje de confirmación
     */
    @PutMapping("/admin/tickets/{id}/responder")
    public ResponseEntity<?> responder(@PathVariable UUID id, @Valid @RequestBody ResponderTicketRequest request) {
        ticketService.responder(id, request.getRespuesta());
        return ResponseEntity.ok(Map.of("mensaje", "Ticket respondido correctamente"));
    }

    /**
     * Cierra un ticket de soporte (acción del administrador).
     * @param id identificador único (UUID) del ticket, tomado de la URL
     * @return 200 OK con un mensaje de confirmación
     */
    @PutMapping("/admin/tickets/{id}/cerrar")
    public ResponseEntity<?> cerrar(@PathVariable UUID id) {
        ticketService.cerrar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Ticket cerrado correctamente"));
    }
}