package com.fsteni58.tennis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/** Acceso a datos del inventario normalizado (inventarios, detalle_inventario, movimientos). */
@Repository
public class InventarioRepository {

    public record StockInventario(UUID productoId, String producto, int stockActual,
                                  int stockMinimo, boolean bajoMinimo) {}

    public record Movimiento(UUID id, String tipo, int cantidad, String motivo, OffsetDateTime fecha) {}

    private final JdbcTemplate jdbcTemplate;

    public InventarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** Stock actual y stock mínimo de cada producto de un proveedor. */
    public List<StockInventario> listarStock(UUID proveedorId) {
        String sql = """
            SELECT producto_id, producto, stock_actual, stock_minimo, bajo_minimo
            FROM v_stock_inventario
            WHERE proveedor_id = ?
            ORDER BY producto
            """;
        return jdbcTemplate.query(sql, (rs, n) -> new StockInventario(
                (UUID) rs.getObject("producto_id"), rs.getString("producto"),
                rs.getInt("stock_actual"), rs.getInt("stock_minimo"),
                rs.getBoolean("bajo_minimo")), proveedorId);
    }

    /** Historial de movimientos de un producto, del más reciente al más antiguo. */
    public List<Movimiento> listarMovimientos(UUID productoId) {
        String sql = """
            SELECT m.id, m.tipo, m.cantidad, m.motivo, m.fecha
            FROM movimientos_inventario m
            JOIN detalle_inventario d ON d.id = m.detalle_inventario_id
            WHERE d.producto_id = ?
            ORDER BY m.fecha DESC
            """;
        return jdbcTemplate.query(sql, (rs, n) -> new Movimiento(
                (UUID) rs.getObject("id"), rs.getString("tipo"), rs.getInt("cantidad"),
                rs.getString("motivo"), rs.getObject("fecha", OffsetDateTime.class)), productoId);
    }

    /**
     * Registra un movimiento. La cantidad ya viene con signo.
     * Los triggers de la base validan que no quede stock negativo y
     * actualizan productos.stock.
     */
    public void registrarMovimiento(UUID productoId, String tipo, int cantidadConSigno, String motivo) {
        String sql = """
            INSERT INTO movimientos_inventario (detalle_inventario_id, tipo, cantidad, motivo)
            VALUES (fn_detalle_inventario_defecto(?), ?, ?, ?)
            """;
        jdbcTemplate.update(sql, productoId, tipo, cantidadConSigno, motivo);
    }

    /** Cambia el stock mínimo (aviso de reposición) del producto. */
    public void actualizarStockMinimo(UUID productoId, int stockMinimo) {
        String sql = "UPDATE detalle_inventario SET stock_minimo = ? WHERE id = fn_detalle_inventario_defecto(?)";
        jdbcTemplate.update(sql, stockMinimo, productoId);
    }
}