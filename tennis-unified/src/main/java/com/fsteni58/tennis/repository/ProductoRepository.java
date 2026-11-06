/*
 * Programa:     ProductoRepository.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Repositorio de acceso a datos del módulo de inventario
 *               (Proveedor US1, registro de productos; Proveedor US5,
 *               actualización de stock). Ejecuta las consultas SQL sobre
 *               la tabla productos vía JdbcTemplate.
 */
package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.ProductoRequest;
import com.fsteni58.tennis.dto.ProductoResponse;
import com.fsteni58.tennis.dto.ProductoCatalogoResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserta un producto nuevo, activo por defecto.
     * @param request datos del producto a registrar
     * @return id generado para el nuevo producto
     */
    public UUID guardar(ProductoRequest request) {
        String sql = """
            INSERT INTO productos (proveedor_id, nombre, descripcion, categoria, precio, stock, activo)
            VALUES (?, ?, ?, ?, ?, ?, true)
            RETURNING id
            """;
        return jdbcTemplate.queryForObject(sql, UUID.class,
                request.getProveedorId(), request.getNombre(), request.getDescripcion(),
                request.getCategoria(), request.getPrecio(), request.getStock());
    }

    /**
     * Actualiza el stock de un producto activo (Proveedor US5).
     * @param productoId id del producto
     * @param nuevoStock nuevo valor de stock
     * @return cantidad de filas afectadas (0 si el producto no existe o no está activo, 1 si se actualizó)
     */
    public int actualizarStock(UUID productoId, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ? AND activo = true";
        return jdbcTemplate.update(sql, nuevoStock, productoId);
    }

    /**
     * Verifica si existe un producto activo con el id dado.
     * @param productoId id del producto
     * @return true si existe y está activo, false en caso contrario
     */
    public boolean existeProducto(UUID productoId) {
        String sql = "SELECT COUNT(*) FROM productos WHERE id = ? AND activo = true";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, productoId);
        return total != null && total > 0;
    }

    /**
     * Lista el id y nombre de todos los productos, activos o no.
     * @return lista de productos
     */
    public List<ProductoResponse> obtenerTodos() {
        String sql = "SELECT id, nombre FROM productos";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductoResponse(
                UUID.fromString(rs.getString("id")),
                rs.getString("nombre")
        ));
    }

    /**
     * Busca un producto por su id, sin filtrar por estado activo.
     * @param id id del producto
     * @return el producto encontrado, o vacío si no existe ningún registro con ese id
     */
    public Optional<ProductoResponse> buscarPorId(UUID id) {
        String sql = "SELECT id, nombre FROM productos WHERE id = ?";
        List<ProductoResponse> resultados = jdbcTemplate.query(sql, (rs, rowNum) -> new ProductoResponse(
            UUID.fromString(rs.getString("id")),
            rs.getString("nombre")
        ), id);
        return resultados.stream().findFirst();
    }

        /**
     * Lista los productos activos del catálogo público, con su imagen.
     * Se ordenan por categoría (raquetas, calzado, accesorios) y luego por nombre.
     * @return lista de productos activos
     */
    public List<ProductoCatalogoResponse> listarCatalogo() {
        String sql = """
            SELECT id, nombre, descripcion, categoria, precio, stock, imagen_url
            FROM productos
            WHERE activo = true
            ORDER BY CASE categoria
                         WHEN 'raquetas' THEN 1
                         WHEN 'calzado' THEN 2
                         WHEN 'accesorios' THEN 3
                         ELSE 4
                     END, nombre
            """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductoCatalogoResponse(
                (UUID) rs.getObject("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("categoria"),
                rs.getBigDecimal("precio"),
                rs.getInt("stock"),
                rs.getString("imagen_url")
        ));
    }

        /**
     * Busca el id del proveedor activo asociado a un usuario.
     * @param usuarioId id del usuario (con rol proveedor)
     * @return el id del proveedor, o vacío si el usuario no es proveedor
     */
    public Optional<UUID> buscarIdPorUsuario(UUID usuarioId) {
        String sql = "SELECT id FROM proveedores WHERE usuario_id = ? AND activo = true";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) return Optional.of((UUID) rs.getObject("id"));
            return Optional.empty();
        }, usuarioId);
    }

}