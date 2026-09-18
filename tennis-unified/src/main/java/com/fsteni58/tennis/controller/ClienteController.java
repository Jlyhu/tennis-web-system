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

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(this::aClienteResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> verUno(@PathVariable UUID id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(aClienteResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarClienteRequest request) {
        clienteService.actualizar(id, request.getNombre(), request.getCorreo());
        return ResponseEntity.ok(new MensajeResponse("Cliente actualizado correctamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> desactivar(@PathVariable UUID id) {
        clienteService.desactivar(id);
        return ResponseEntity.ok(new MensajeResponse("Cliente desactivado correctamente"));
    }

    private ClienteResponse aClienteResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.isActivo()
        );
    }
}