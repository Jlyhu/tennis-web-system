/*
 * Programa:     PedidoController.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Controlador REST de gestión de pedidos. Expone las
 *               operaciones de crear una orden, consultarla por
 *               identificador, listar órdenes (con filtro opcional por
 *               estado) y actualizar el estado de una orden, bajo la ruta
 *               /api/pedidos. Recibe las peticiones HTTP y delega toda la
 *               lógica de negocio a PedidoService.
 */
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

    /**
     * Crea una nueva orden de pedido. La lógica de creación (validación de
     * los datos, cálculo y persistencia) se realiza en PedidoService.
     * @param request datos de la orden a crear
     * @return 201 Created con la orden generada
     */
    @PostMapping
    public ResponseEntity<OrdenPedidoResponse> crearOrden(@RequestBody OrdenPedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearOrden(request));
    }

    /**
     * Consulta una orden de pedido por su identificador.
     * @param id identificador único (UUID) de la orden, tomado de la URL
     * @return 200 OK con los datos de la orden
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdenPedidoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    /**
     * Lista las órdenes de pedido. Si se envía el parámetro estado, solo
     * devuelve las órdenes que se encuentren en ese estado; si no, devuelve
     * todas.
     * @param estado estado por el cual filtrar (opcional, parámetro de consulta)
     * @return 200 OK con la lista de órdenes
     */
    @GetMapping
    public ResponseEntity<List<OrdenPedidoResponse>> obtenerTodas(
            @RequestParam(required = false) EstadoOrden estado) {
        if (estado != null) {
            return ResponseEntity.ok(pedidoService.obtenerPorEstado(estado));
        }
        return ResponseEntity.ok(pedidoService.obtenerTodas());
    }

    /**
     * Actualiza el estado de una orden existente.
     * @param id          identificador único (UUID) de la orden, tomado de la URL
     * @param nuevoEstado nuevo estado que se le asignará a la orden (parámetro de consulta)
     * @return 200 OK con la orden actualizada
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenPedidoResponse> actualizarEstado(
            @PathVariable UUID id,
            @RequestParam EstadoOrden nuevoEstado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, nuevoEstado));
    }
}