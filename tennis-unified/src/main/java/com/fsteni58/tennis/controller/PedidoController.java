package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.OrdenPedidoRequest;
import com.fsteni58.tennis.dto.OrdenPedidoResponse;
import com.fsteni58.tennis.model.EstadoOrden;
import com.fsteni58.tennis.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<OrdenPedidoResponse> crearOrden(@RequestBody OrdenPedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearOrden(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenPedidoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<OrdenPedidoResponse>> obtenerTodas(
            @RequestParam(required = false) EstadoOrden estado) {
        if (estado != null) {
            return ResponseEntity.ok(pedidoService.obtenerPorEstado(estado));
        }
        return ResponseEntity.ok(pedidoService.obtenerTodas());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenPedidoResponse> actualizarEstado(
            @PathVariable UUID id,
            @RequestParam EstadoOrden nuevoEstado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, nuevoEstado));
    }
}