/*
 * Programa:     TicketSoporteServiceTest.java
 * Versión:      1.0
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Pruebas unitarias (caja blanca) de TicketSoporteService.responder
 *               y TicketSoporteService.cerrar (Administrador US10). El repositorio
 *               se simula con Mockito, por lo que no se requiere base de datos.
 */
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.exception.TicketNoEncontradoException;
import com.fsteni58.tennis.model.TicketSoporte;
import com.fsteni58.tennis.repository.TicketSoporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketSoporteServiceTest {

    @Mock
    private TicketSoporteRepository ticketRepository;

    private TicketSoporteService ticketService;
    private final UUID ticketId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ticketService = new TicketSoporteService(ticketRepository);
    }

    private TicketSoporte ticketConEstado(String estado) {
        TicketSoporte ticket = new TicketSoporte();
        ticket.setId(ticketId);
        ticket.setEstado(estado);
        return ticket;
    }

    // ---------- PU-001: responder ----------

    // Caso Válido: texto normal sobre un ticket existente
    @Test
    void responder_textoValido_guardaRespuestaEnRepositorio() {
        String respuesta = "Estamos verificando la base de datos, te confirmamos en breve";
        when(ticketRepository.buscarPorId(ticketId))
                .thenReturn(Optional.of(ticketConEstado("abierto")));

        assertDoesNotThrow(() -> ticketService.responder(ticketId, respuesta));

        verify(ticketRepository).responder(ticketId, respuesta);
    }

    // Caso Inválido: ticket inexistente
    @Test
    void responder_ticketInexistente_lanzaTicketNoEncontradoYNoGuarda() {
        when(ticketRepository.buscarPorId(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNoEncontradoException.class,
                () -> ticketService.responder(ticketId, "Respuesta de prueba"));

        verify(ticketRepository, never()).responder(any(), any());
    }

    // Caso Vacío: id ausente (null); el repositorio simulado responde Optional.empty()
    @Test
    void responder_idNulo_lanzaTicketNoEncontradoYNoGuarda() {
        assertThrows(TicketNoEncontradoException.class,
                () -> ticketService.responder(null, "Respuesta de prueba"));

        verify(ticketRepository, never()).responder(any(), any());
    }

    // Caso Límite: respuesta de un solo carácter, el mínimo que deja pasar @NotBlank
    @Test
    void responder_unSoloCaracter_guardaRespuestaEnRepositorio() {
        when(ticketRepository.buscarPorId(ticketId))
                .thenReturn(Optional.of(ticketConEstado("abierto")));

        assertDoesNotThrow(() -> ticketService.responder(ticketId, "a"));

        verify(ticketRepository).responder(ticketId, "a");
    }

    // ---------- PU-002: cerrar ----------

    // Caso Válido: cerrar un ticket respondido
    @Test
    void cerrar_ticketRespondido_cierraEnRepositorio() {
        when(ticketRepository.buscarPorId(ticketId))
                .thenReturn(Optional.of(ticketConEstado("respondido")));

        assertDoesNotThrow(() -> ticketService.cerrar(ticketId));

        verify(ticketRepository).cerrar(ticketId);
    }

    // Caso Inválido: ticket inexistente
    @Test
    void cerrar_ticketInexistente_lanzaTicketNoEncontradoYNoCierra() {
        when(ticketRepository.buscarPorId(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNoEncontradoException.class,
                () -> ticketService.cerrar(ticketId));

        verify(ticketRepository, never()).cerrar(any());
    }

    // Caso Vacío: id ausente (null)
    @Test
    void cerrar_idNulo_lanzaTicketNoEncontradoYNoCierra() {
        assertThrows(TicketNoEncontradoException.class,
                () -> ticketService.cerrar(null));

        verify(ticketRepository, never()).cerrar(any());
    }

    // Caso Límite: cerrar un ticket que ya está cerrado
    @Test
    void cerrar_ticketYaCerrado_noLanzaExcepcion() {
        when(ticketRepository.buscarPorId(ticketId))
                .thenReturn(Optional.of(ticketConEstado("cerrado")));

        assertDoesNotThrow(() -> ticketService.cerrar(ticketId));

        verify(ticketRepository).cerrar(ticketId);
    }

        // ---------- PU-003: crearTicket ----------

    // Caso Válido: comprador, asunto y descripción normales
    @Test
    void crearTicket_datosValidos_delegaEnRepositorioYRetornaId() {
        UUID compradorId = UUID.randomUUID();
        UUID idEsperado = UUID.randomUUID();
        when(ticketRepository.crear(compradorId, "Consulta sobre envío", "¿Cuánto tarda?"))
                .thenReturn(idEsperado);

        UUID resultado = ticketService.crearTicket(compradorId, "Consulta sobre envío", "¿Cuánto tarda?");

        assertEquals(idEsperado, resultado);
        verify(ticketRepository).crear(compradorId, "Consulta sobre envío", "¿Cuánto tarda?");
    }

    // Caso Inválido: compradorId nulo — el Service no lo valida, a diferencia de responder/cerrar
    @Test
    void crearTicket_compradorIdNulo_noValidaYDelegaIgual() {
        when(ticketRepository.crear(null, "Asunto", "Descripción"))
                .thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> ticketService.crearTicket(null, "Asunto", "Descripción"));

        verify(ticketRepository).crear(null, "Asunto", "Descripción");
    }

    // Caso Vacío: asunto en blanco — el Service tampoco lo valida
    @Test
    void crearTicket_asuntoVacio_noValidaYDelegaIgual() {
        UUID compradorId = UUID.randomUUID();
        when(ticketRepository.crear(compradorId, "", "Descripción"))
                .thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> ticketService.crearTicket(compradorId, "", "Descripción"));

        verify(ticketRepository).crear(compradorId, "", "Descripción");
    }

    // Caso Límite: descripción extremadamente larga (5000 caracteres)
    @Test
    void crearTicket_descripcionMuyLarga_delegaSinTruncar() {
        UUID compradorId = UUID.randomUUID();
        String descripcionLarga = "a".repeat(5000);
        when(ticketRepository.crear(compradorId, "Asunto", descripcionLarga))
                .thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> ticketService.crearTicket(compradorId, "Asunto", descripcionLarga));

        verify(ticketRepository).crear(compradorId, "Asunto", descripcionLarga);
    }
}