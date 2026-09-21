package com.fsteni58.tennis.service;

import com.fsteni58.tennis.exception.TicketNoEncontradoException;
import com.fsteni58.tennis.model.TicketSoporte;
import com.fsteni58.tennis.repository.TicketSoporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketSoporteService {

    private final TicketSoporteRepository ticketRepository;

    public TicketSoporteService(TicketSoporteRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // El comprador reporta un problema
    public UUID crearTicket(UUID compradorId, String asunto, String descripcion) {
        return ticketRepository.crear(compradorId, asunto, descripcion);
    }

    // El administrador ve todos
    public List<TicketSoporte> listarTodos() {
        return ticketRepository.listarTodos();
    }

    // El comprador ve solo los suyos
    public List<TicketSoporte> listarPorComprador(UUID compradorId) {
        return ticketRepository.listarPorComprador(compradorId);
    }

    public TicketSoporte buscarPorId(UUID id) {
        return ticketRepository.buscarPorId(id)
                .orElseThrow(() -> new TicketNoEncontradoException("No existe un ticket con id " + id));
    }

    // El administrador responde
    public void responder(UUID id, String respuesta) {
        buscarPorId(id); // confirma que existe
        ticketRepository.responder(id, respuesta);
    }

    public void cerrar(UUID id) {
        buscarPorId(id);
        ticketRepository.cerrar(id);
    }
}
