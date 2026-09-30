/*
 * Programa:     ClienteController.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Controlador REST de gestión de clientes para el
 *               administrador. Expone las operaciones de listar, consultar,
 *               actualizar y desactivar clientes bajo la ruta
 *               /admin/clientes. Recibe las peticiones HTTP y delega toda
 *               la lógica de negocio a ClienteService; la desactivación es
 *               lógica (el cliente no se elimina de la base de datos).
 */
package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.ActualizarClienteRequest;
import com.fsteni58.tennis.dto.ClienteResponse;
import com.fsteni58.tennis.dto.MensajeResponse;
import com.fsteni58.tennis.model.Cliente;
import com.fsteni58.tennis.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Lista todos los clientes registrados. Cada cliente se convierte a
     * ClienteResponse para no exponer la entidad del modelo directamente.
     * @return 200 OK con la lista de clientes (vacía si no hay ninguno)
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(this::aClienteResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    /**
     * Consulta los datos de un cliente por su identificador.
     * @param id identificador único (UUID) del cliente, tomado de la URL
     * @return 200 OK con los datos del cliente
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> verUno(@PathVariable UUID id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(aClienteResponse(cliente));
    }

    /**
     * Actualiza el nombre y el correo de un cliente existente. La
     * validación de los campos del body la aplica @Valid sobre
     * ActualizarClienteRequest antes de que este método se ejecute.
     * @param id      identificador único (UUID) del cliente, tomado de la URL
     * @param request nuevos datos del cliente (nombre y correo)
     * @return 200 OK con un mensaje de confirmación
     */
    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarClienteRequest request) {
        clienteService.actualizar(id, request.getNombre(), request.getCorreo());
        return ResponseEntity.ok(new MensajeResponse("Cliente actualizado correctamente"));
    }

    /**
     * Desactiva un cliente (borrado lógico): el registro se conserva, pero
     * el cliente queda marcado como inactivo.
     * @param id identificador único (UUID) del cliente, tomado de la URL
     * @return 200 OK con un mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> desactivar(@PathVariable UUID id) {
        clienteService.desactivar(id);
        return ResponseEntity.ok(new MensajeResponse("Cliente desactivado correctamente"));
    }

    /**
     * Convierte una entidad Cliente en su representación de respuesta.
     * @param cliente entidad del modelo a convertir
     * @return ClienteResponse con id, nombre, correo y estado activo
     */
    private ClienteResponse aClienteResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.isActivo()
        );
    }
}