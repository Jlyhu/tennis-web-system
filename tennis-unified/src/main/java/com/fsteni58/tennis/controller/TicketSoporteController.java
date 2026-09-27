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

    // El comprador reporta un problema
    @PostMapping("/tickets")
    public ResponseEntity<?> crear(@Valid @RequestBody CrearTicketRequest request) {
        UUID idGenerado = ticketService.crearTicket(
                request.getCompradorId(), request.getAsunto(), request.getDescripcion());
        return ResponseEntity.ok(Map.of("id", idGenerado, "mensaje", "Ticket creado correctamente"));
    }

    // El comprador ve sus propios tickets
    @GetMapping("/tickets/mios")
    public List<TicketSoporte> misTickets(@RequestParam UUID compradorId) {
        return ticketService.listarPorComprador(compradorId);
    }

    // El administrador ve todos los tickets
    @GetMapping("/admin/tickets")
    public List<TicketSoporte> listarTodos() {
        return ticketService.listarTodos();
    }

    @GetMapping("/admin/tickets/{id}")
    public TicketSoporte verUno(@PathVariable UUID id) {
        return ticketService.buscarPorId(id);
    }

    // El administrador responde
    @PutMapping("/admin/tickets/{id}/responder")
    public ResponseEntity<?> responder(@PathVariable UUID id, @Valid @RequestBody ResponderTicketRequest request) {
        ticketService.responder(id, request.getRespuesta());
        return ResponseEntity.ok(Map.of("mensaje", "Ticket respondido correctamente"));
    }

    @PutMapping("/admin/tickets/{id}/cerrar")
    public ResponseEntity<?> cerrar(@PathVariable UUID id) {
        ticketService.cerrar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Ticket cerrado correctamente"));
    }
}
