/*
 * Programa:     ClienteService.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de negocio de la gestión de clientes (Administrador
 *               US5). Un "cliente" es un usuario con rol comprador; este
 *               servicio permite consultarlos, actualizarlos y
 *               desactivarlos (borrado lógico).
 */
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.exception.ClienteNoEncontradoException;
import com.fsteni58.tennis.model.Cliente;
import com.fsteni58.tennis.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Lista todos los clientes registrados.
     * @return lista de clientes, ordenada por nombre
     */
    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    /**
     * Busca un cliente por su id.
     * @param id id del cliente
     * @return el cliente encontrado
     * @throws ClienteNoEncontradoException si no existe un cliente con ese id
     */
    public Cliente buscarPorId(UUID id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No existe un cliente con id " + id));
    }

    /**
     * Actualiza el nombre y correo de un cliente existente.
     * @param id id del cliente a actualizar
     * @param nombre nuevo nombre, no puede estar vacío
     * @param correo nuevo correo, no puede estar vacío
     * @throws IllegalArgumentException si nombre o correo vienen vacíos
     * @throws ClienteNoEncontradoException si no existe un cliente con ese id
     */
    public void actualizar(UUID id, String nombre, String correo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        buscarPorId(id);
        clienteRepository.actualizar(id, nombre, correo);
    }

    /**
     * Desactiva un cliente (borrado lógico).
     * @param id id del cliente a desactivar
     * @throws ClienteNoEncontradoException si no existe un cliente con ese id
     */
    public void desactivar(UUID id) {
        buscarPorId(id);
        clienteRepository.desactivar(id);
    }
}