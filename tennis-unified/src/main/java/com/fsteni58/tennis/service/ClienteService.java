package com.fsteni58.tennis.service;

import com.fsteni58.tennis.exception.ClienteNoEncontradoException;
import com.fsteni58.tennis.model.Cliente;
import com.fsteni58.tennis.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// Administrador US5: lógica de negocio de la gestión de clientes
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    public Cliente buscarPorId(UUID id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No existe un cliente con id " + id));
    }

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

    public void desactivar(UUID id) {
        buscarPorId(id);
        clienteRepository.desactivar(id);
    }
}
