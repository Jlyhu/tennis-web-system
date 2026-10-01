/*
 * Programa:     PedidoService.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de negocio del módulo de gestión de pedidos
 *               (Proveedor US2, consultar y gestionar órdenes; Proveedor
 *               US3, aceptar o rechazar pedidos). Calcula el total de la
 *               orden a partir de los ítems recibidos y controla las
 *               transiciones de estado permitidas.
 */
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.ItemPedidoDTO;
import com.fsteni58.tennis.dto.OrdenPedidoRequest;
import com.fsteni58.tennis.dto.OrdenPedidoResponse;
import com.fsteni58.tennis.exception.PedidoException;
import com.fsteni58.tennis.model.EstadoOrden;
import com.fsteni58.tennis.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Crea una nueva orden de pedido en estado PENDIENTE, calculando el
     * total como la suma de los subtotales de cada ítem.
     * @param request datos de la orden: cliente e ítems
     * @return la orden creada, con id, fecha y total calculados
     * @throws PedidoException si el cliente viene vacío, o si no se incluye al menos un ítem
     */
    public OrdenPedidoResponse crearOrden(OrdenPedidoRequest request) {
        if (request.cliente() == null || request.cliente().isBlank()) {
            throw new PedidoException("El cliente no puede estar vacío.");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw new PedidoException("La orden debe contener al menos un ítem.");
        }

        BigDecimal total = request.items().stream()
                .map(ItemPedidoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrdenPedidoResponse nuevaOrden = new OrdenPedidoResponse(
                UUID.randomUUID(),
                request.cliente(),
                LocalDate.now(),
                EstadoOrden.PENDIENTE,
                request.items(),
                total
        );

        return pedidoRepository.guardar(nuevaOrden);
    }

    /**
     * Busca una orden por su id.
     * @param id id de la orden
     * @return la orden encontrada
     * @throws PedidoException si no existe una orden con ese id
     */
    public OrdenPedidoResponse buscarPorId(UUID id) {
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new PedidoException("No existe una orden con el ID " + id));
    }

    /**
     * Lista todas las órdenes registradas.
     * @return lista completa de órdenes
     */
    public List<OrdenPedidoResponse> obtenerTodas() {
        return pedidoRepository.obtenerTodas();
    }

    /**
     * Lista las órdenes que se encuentran en un estado específico.
     * @param estado estado por el cual filtrar
     * @return lista de órdenes con ese estado
     */
    public List<OrdenPedidoResponse> obtenerPorEstado(EstadoOrden estado) {
        return pedidoRepository.obtenerPorEstado(estado);
    }

    /**
     * Cambia el estado de una orden existente (Proveedor US3, aceptar o
     * rechazar pedidos). No permite cambiar el estado de una orden que ya
     * esté ENTREGADO o CANCELADO.
     * @param id id de la orden
     * @param nuevoEstado estado al que se quiere cambiar
     * @return la orden con el estado ya actualizado
     * @throws PedidoException si no existe la orden, o si su estado actual es ENTREGADO o CANCELADO
     */
    public OrdenPedidoResponse actualizarEstado(UUID id, EstadoOrden nuevoEstado) {
        OrdenPedidoResponse orden = buscarPorId(id);

        if (orden.estado() == EstadoOrden.ENTREGADO || orden.estado() == EstadoOrden.CANCELADO) {
            throw new PedidoException("No se puede cambiar el estado de una orden " + orden.estado());
        }

        return pedidoRepository.actualizarEstado(id, nuevoEstado);
    }
}