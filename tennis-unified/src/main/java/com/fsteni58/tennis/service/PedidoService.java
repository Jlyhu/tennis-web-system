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

    public OrdenPedidoResponse buscarPorId(UUID id) {
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new PedidoException("No existe una orden con el ID " + id));
    }

    public List<OrdenPedidoResponse> obtenerTodas() {
        return pedidoRepository.obtenerTodas();
    }

    public List<OrdenPedidoResponse> obtenerPorEstado(EstadoOrden estado) {
        return pedidoRepository.obtenerPorEstado(estado);
    }

    public OrdenPedidoResponse actualizarEstado(UUID id, EstadoOrden nuevoEstado) {
        OrdenPedidoResponse orden = buscarPorId(id);

        if (orden.estado() == EstadoOrden.ENTREGADO || orden.estado() == EstadoOrden.CANCELADO) {
            throw new PedidoException("No se puede cambiar el estado de una orden " + orden.estado());
        }

        return pedidoRepository.actualizarEstado(id, nuevoEstado);
    }
}