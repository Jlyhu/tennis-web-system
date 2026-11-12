/*
 * Programa:     CarritoRepository.java
 * Versión:      1.0
 * Fecha:        06/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Repositorio de acceso a datos del carrito de compras
 *               (Comprador). Ejecuta las consultas SQL sobre las tablas
 *               carritos y carrito_items vía JdbcTemplate; también lee y
 *               descuenta el stock de productos para validar y pagar.
 */
package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.CarritoItemResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CarritoRepository {

    private final JdbcTemplate jdbcTemplate;

    public CarritoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Verifica que el usuario exista, esté activo y tenga rol comprador.
     * @param usuarioId id del usuario
     * @return true si es un comprador activo, false en caso contrario
     */
    public boolean existeComprador(UUID usuarioId) {
        String sql = """
            SELECT COUNT(*)
            FROM usuarios u
            JOIN roles r ON u.rol_id = r.id
            WHERE u.id = ? AND u.activo = true AND r.nombre_rol = 'comprador'
            """;
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, usuarioId);
        return total != null && total > 0;
    }

    /**
     * Busca el id del carrito activo de un usuario.
     * @param usuarioId id del usuario
     * @return el id del carrito, o vacío si no tiene un carrito activo
     */
    public Optional<UUID> buscarCarritoActivo(UUID usuarioId) {
        String sql = "SELECT id FROM carritos WHERE usuario_id = ? AND estado = 'activo'";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) return Optional.of((UUID) rs.getObject("id"));
            return Optional.empty();
        }, usuarioId);
    }

    /**
     * Devuelve el carrito activo del usuario; si no tiene, lo crea. El
     * ON CONFLICT evita que dos peticiones simultáneas creen dos carritos.
     * @param usuarioId id del usuario
     * @return id del carrito activo
     */
    public UUID obtenerOCrearCarritoActivo(UUID usuarioId) {
        jdbcTemplate.update("""
            INSERT INTO carritos (usuario_id, estado)
            VALUES (?, 'activo')
            ON CONFLICT (usuario_id) WHERE estado = 'activo' DO NOTHING
            """, usuarioId);
        return buscarCarritoActivo(usuarioId)
                .orElseThrow(() -> new IllegalStateException("No se pudo crear el carrito"));
    }

    /**
     * Lee el stock de un producto activo.
     * @param productoId id del producto
     * @return el stock, o vacío si el producto no existe o no está activo
     */
    public Optional<Integer> stockDeProductoActivo(UUID productoId) {
        String sql = "SELECT stock FROM productos WHERE id = ? AND activo = true";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) return Optional.of(rs.getInt("stock"));
            return Optional.empty();
        }, productoId);
    }

    /**
     * Cuenta cuántas unidades de un producto hay ya en un carrito.
     * @param carritoId id del carrito
     * @param productoId id del producto
     * @return unidades en el carrito (0 si el producto no está)
     */
    public int cantidadEnCarrito(UUID carritoId, UUID productoId) {
        String sql = "SELECT COALESCE(SUM(cantidad), 0) FROM carrito_items WHERE carrito_id = ? AND producto_id = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, carritoId, productoId);
        return total == null ? 0 : total;
    }

    /**
     * Agrega un producto al carrito; si ya estaba, suma la cantidad.
     * @param carritoId id del carrito
     * @param productoId id del producto
     * @param cantidad unidades a agregar
     */
    public void agregarOSumar(UUID carritoId, UUID productoId, int cantidad) {
        String sql = """
            INSERT INTO carrito_items (carrito_id, producto_id, cantidad)
            VALUES (?, ?, ?)
            ON CONFLICT (carrito_id, producto_id)
            DO UPDATE SET cantidad = carrito_items.cantidad + EXCLUDED.cantidad
            """;
        jdbcTemplate.update(sql, carritoId, productoId, cantidad);
    }

    /**
     * Cambia la cantidad de un producto que ya está en el carrito.
     * @param carritoId id del carrito
     * @param productoId id del producto
     * @param cantidad nueva cantidad
     * @return filas afectadas (0 si el producto no está en el carrito)
     */
    public int actualizarCantidad(UUID carritoId, UUID productoId, int cantidad) {
        String sql = "UPDATE carrito_items SET cantidad = ? WHERE carrito_id = ? AND producto_id = ?";
        return jdbcTemplate.update(sql, cantidad, carritoId, productoId);
    }

    /**
     * Quita un producto del carrito.
     * @param carritoId id del carrito
     * @param productoId id del producto
     * @return filas afectadas (0 si el producto no estaba)
     */
    public int quitarItem(UUID carritoId, UUID productoId) {
        String sql = "DELETE FROM carrito_items WHERE carrito_id = ? AND producto_id = ?";
        return jdbcTemplate.update(sql, carritoId, productoId);
    }

    /**
     * Quita todos los productos de un carrito.
     * @param carritoId id del carrito
     * @return filas afectadas
     */
    public int vaciar(UUID carritoId) {
        return jdbcTemplate.update("DELETE FROM carrito_items WHERE carrito_id = ?", carritoId);
    }

    /**
     * Lista los productos de un carrito, con los datos del producto, en el
     * orden en que se agregaron.
     * @param carritoId id del carrito
     * @return lista de ítems (vacía si no hay ninguno)
     */
    public List<CarritoItemResponse> listarItems(UUID carritoId) {
        String sql = """
            SELECT p.id, p.nombre, p.imagen_url, p.precio, p.stock, ci.cantidad
            FROM carrito_items ci
            JOIN productos p ON p.id = ci.producto_id
            WHERE ci.carrito_id = ?
            ORDER BY ci.fecha_agregado
            """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CarritoItemResponse(
                (UUID) rs.getObject("id"),
                rs.getString("nombre"),
                rs.getString("imagen_url"),
                rs.getBigDecimal("precio"),
                rs.getInt("cantidad"),
                rs.getInt("stock")
        ), carritoId);
    }

    /**
     * Descuenta unidades del stock de un producto, solo si alcanza.
     * @param productoId id del producto
     * @param cantidad unidades a descontar
     * @return filas afectadas (0 si el producto no está activo o no hay stock suficiente)
     */
    

    /**
     * Marca un carrito como comprado, para que el usuario pueda abrir uno nuevo.
     * @param carritoId id del carrito
     * @return filas afectadas
     */
        public int marcarComprado(UUID carritoId) {
        return jdbcTemplate.update("UPDATE carritos SET estado = 'comprado' WHERE id = ? AND estado = 'activo'", carritoId);
    }

    /** Factura emitida: número consecutivo y valores. */
    public record FacturaEmitida(long numero, BigDecimal subtotal, BigDecimal iva, BigDecimal total) { }

    /** Busca el id de un método de pago activo por su código (nequi, tarjeta, paypal). */
    public Optional<UUID> buscarMetodoPago(String codigo) {
        return jdbcTemplate.query("SELECT id FROM metodos_pago WHERE codigo = ? AND activo = true", rs -> {
            if (rs.next()) return Optional.of((UUID) rs.getObject("id"));
            return Optional.empty();
        }, codigo);
    }

    /** Crea el pedido ya pagado (estado EN_PROCESO) y devuelve su id. */
    public UUID crearPedido(UUID compradorId, UUID metodoPagoId, String referencia) {
        return jdbcTemplate.queryForObject("""
            INSERT INTO pedidos (comprador_id, metodo_pago_id, referencia, estado, fecha_pago)
            VALUES (?, ?, ?, 'EN_PROCESO', now())
            RETURNING id
            """, UUID.class, compradorId, metodoPagoId, referencia);
    }

    /** Agrega una línea al pedido. El trigger de la base completa proveedor, nombre y subtotal. */
    public void agregarLinea(UUID pedidoId, UUID productoId, int cantidad, BigDecimal precioUnitario) {
        jdbcTemplate.update("""
            INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario)
            VALUES (?, ?, ?, ?)
            """, pedidoId, productoId, cantidad, precioUnitario);
    }

    /** Registra las salidas de inventario del pedido; el stock se actualiza solo. */
    public void registrarSalidas(UUID pedidoId) {
        jdbcTemplate.query("SELECT fn_registrar_salidas_pedido(?::uuid)", rs -> { }, pedidoId);
    }

    /** Emite la factura del pedido con el IVA y la comisión indicados. */
    public UUID crearFactura(UUID pedidoId, BigDecimal iva, BigDecimal comision) {
        return jdbcTemplate.queryForObject(
                "SELECT fn_crear_factura(?::uuid, ?::numeric, ?::numeric)", UUID.class, pedidoId, iva, comision);
    }

    public FacturaEmitida leerFactura(UUID facturaId) {
        return jdbcTemplate.queryForObject(
                "SELECT numero, subtotal, iva, total FROM facturas WHERE id = ?",
                (rs, n) -> new FacturaEmitida(rs.getLong("numero"), rs.getBigDecimal("subtotal"),
                        rs.getBigDecimal("iva"), rs.getBigDecimal("total")),
                facturaId);
    }

    /** El total del pedido es el de la factura (con IVA). */
    public void fijarTotalPedido(UUID pedidoId, BigDecimal total) {
        jdbcTemplate.update("UPDATE pedidos SET total = ? WHERE id = ?", total, pedidoId);
    }
}