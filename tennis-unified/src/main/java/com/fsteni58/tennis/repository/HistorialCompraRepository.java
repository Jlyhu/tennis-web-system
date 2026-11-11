
package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.CompraResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HistorialCompraRepository {

    private final JdbcTemplate jdbcTemplate;

    public HistorialCompraRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CompraResponse> listarPorComprador(UUID compradorId) {

        String sql = """
            SELECT
                p.id AS pedido_id,
                p.fecha_creacion,
                p.estado,
                p.total,
                d.producto_id,
                d.nombre_producto,
                d.cantidad,
                d.precio_unitario,
                d.subtotal
            FROM pedidos p
            LEFT JOIN detalle_pedido d
                ON d.pedido_id = p.id
            WHERE p.comprador_id = ?
            ORDER BY p.fecha_creacion DESC
            """;

        Map<UUID, List<CompraResponse.ItemCompraResponse>> detalles =
                new LinkedHashMap<>();

        Map<UUID, CompraResponse> pedidos = new LinkedHashMap<>();

        jdbcTemplate.query(sql, rs -> {

            UUID pedidoId = rs.getObject("pedido_id", UUID.class);

            pedidos.putIfAbsent(pedidoId,
                new CompraResponse(
                    pedidoId,
                    rs.getObject("fecha_creacion",
                        java.time.OffsetDateTime.class),
                    rs.getString("estado"),
                    rs.getBigDecimal("total"),
                    new ArrayList<>()
                )
            );

            if (rs.getString("nombre_producto") != null) {

                detalles.computeIfAbsent(
                    pedidoId, k -> new ArrayList<>()
                ).add(
                    new CompraResponse.ItemCompraResponse(
                        rs.getObject("producto_id", UUID.class),
                        rs.getString("nombre_producto"),
                        rs.getInt("cantidad"),
                        rs.getBigDecimal("precio_unitario"),
                        rs.getBigDecimal("subtotal")
                    )
                );
            }
        }, compradorId);

        List<CompraResponse> resultado = new ArrayList<>();

        pedidos.forEach((id, pedido) -> {
            resultado.add(new CompraResponse(
                pedido.id(),
                pedido.fecha(),
                pedido.estado(),
                pedido.total(),
                detalles.getOrDefault(id, new ArrayList<>())
            ));
        });

        return resultado;
    }
}
