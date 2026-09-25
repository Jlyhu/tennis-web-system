package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.OrdenPedidoResponse;
import com.fsteni58.tennis.model.EstadoOrden;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PedidoRepository {

    private final JdbcTemplate jdbcTemplate;
    // Lista en memoria o implementación JDBC según la BD
    private final List<OrdenPedidoResponse> ordenesBD = new ArrayList<>();

    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public OrdenPedidoResponse guardar(OrdenPedidoResponse orden) {
        ordenesBD.add(orden);
        return orden;
    }

    public Optional<OrdenPedidoResponse> buscarPorId(UUID id) {
        return ordenesBD.stream()
                .filter(o -> o.id().equals(id))
                .findFirst();
    }

    public List<OrdenPedidoResponse> obtenerTodas() {
        return new ArrayList<>(ordenesBD);
    }

    public List<OrdenPedidoResponse> obtenerPorEstado(EstadoOrden estado) {
        return ordenesBD.stream()
                .filter(o -> o.estado() == estado)
                .toList();
    }

    public OrdenPedidoResponse actualizarEstado(UUID id, EstadoOrden nuevoEstado) {
        for (int i = 0; i < ordenesBD.size(); i++) {
            if (ordenesBD.get(i).id().equals(id)) {
                OrdenPedidoResponse anterior = ordenesBD.get(i);
                OrdenPedidoResponse actualizada = new OrdenPedidoResponse(
                        anterior.id(),
                        anterior.cliente(),
                        anterior.fecha(),
                        nuevoEstado,
                        anterior.items(),
                        anterior.total()
                );
                ordenesBD.set(i, actualizada);
                return actualizada;
            }
        }
        return null;
    }
}