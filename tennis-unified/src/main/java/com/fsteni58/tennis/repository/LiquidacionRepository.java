package com.fsteni58.tennis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Acceso a datos de la liquidación a proveedores (usa fn_liquidar_proveedor de la base). */
@Repository
public class LiquidacionRepository {

    public record Liquidacion(UUID id, UUID proveedorId, String proveedor, String tipo, String motivo,
                              LocalDate fechaInicio, LocalDate fechaFin, BigDecimal total, String estado) {}

    public record Linea(String producto, int cantidad, long factura, BigDecimal monto) {}

    public record ProveedorOpcion(UUID id, String nombre) {}

    public record MotivoSalida(UUID id, String nombre) {}

    private final JdbcTemplate jdbcTemplate;

    public LiquidacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** Crea la liquidación en la base; no repite líneas ya liquidadas. */
    public UUID crear(UUID proveedorId, LocalDate desde, LocalDate hasta, String tipo, UUID motivoId) {
        String sql = "SELECT fn_liquidar_proveedor(?::uuid, ?::date, ?::date, ?::varchar, ?::uuid)";
        return jdbcTemplate.queryForObject(sql, UUID.class, proveedorId.toString(),
                desde.toString(), hasta.toString(), tipo, motivoId == null ? null : motivoId.toString());
    }

    public List<Liquidacion> listar() {
        String sql = """
            SELECT l.id, l.proveedor_id, p.nombre_empresa, l.tipo, m.nombre AS motivo,
                   l.fecha_inicio, l.fecha_fin, l.total, l.estado
            FROM liquidaciones_proveedor l
            JOIN proveedores p ON p.id = l.proveedor_id
            LEFT JOIN motivos_salida m ON m.id = l.motivo_salida_id
            WHERE l.activo
            ORDER BY l.fecha_creacion DESC
            """;
        return jdbcTemplate.query(sql, (rs, n) -> new Liquidacion(
                (UUID) rs.getObject("id"), (UUID) rs.getObject("proveedor_id"),
                rs.getString("nombre_empresa"), rs.getString("tipo"), rs.getString("motivo"),
                rs.getObject("fecha_inicio", LocalDate.class), rs.getObject("fecha_fin", LocalDate.class),
                rs.getBigDecimal("total"), rs.getString("estado")));
    }

    public List<Linea> detalle(UUID liquidacionId) {
        String sql = """
            SELECT pr.nombre, dp.cantidad, f.numero, dl.monto
            FROM detalle_liquidacion dl
            JOIN detalle_factura df ON df.id = dl.detalle_factura_id
            JOIN facturas f ON f.id = df.factura_id
            JOIN detalle_pedido dp ON dp.id = df.detalle_pedido_id
            JOIN productos pr ON pr.id = dp.producto_id
            WHERE dl.liquidacion_id = ?
            ORDER BY f.numero
            """;
        return jdbcTemplate.query(sql, (rs, n) -> new Linea(
                rs.getString("nombre"), rs.getInt("cantidad"), rs.getLong("numero"),
                rs.getBigDecimal("monto")), liquidacionId);
    }

    /** Marca como pagada una liquidación pendiente. @return filas afectadas (0 si no existe o ya estaba pagada) */
    public int marcarPagada(UUID liquidacionId) {
        return jdbcTemplate.update(
                "UPDATE liquidaciones_proveedor SET estado = 'pagada' WHERE id = ? AND estado = 'pendiente' AND activo",
                liquidacionId);
    }

    public List<ProveedorOpcion> proveedoresActivos() {
        return jdbcTemplate.query("SELECT id, nombre_empresa FROM proveedores WHERE activo ORDER BY nombre_empresa",
                (rs, n) -> new ProveedorOpcion((UUID) rs.getObject("id"), rs.getString("nombre_empresa")));
    }

    public List<MotivoSalida> motivosSalida() {
        return jdbcTemplate.query("SELECT id, nombre FROM motivos_salida WHERE activo ORDER BY nombre",
                (rs, n) -> new MotivoSalida((UUID) rs.getObject("id"), rs.getString("nombre")));
    }
}