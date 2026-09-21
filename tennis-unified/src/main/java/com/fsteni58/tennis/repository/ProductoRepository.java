package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.ProductoRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ProductoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public int actualizarStock(UUID productoId, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ? AND activo = true";
        return jdbcTemplate.update(sql, nuevoStock, productoId);
    }

    public boolean existeProducto(UUID productoId) {
        String sql = "SELECT COUNT(*) FROM productos WHERE id = ? AND activo = true";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, productoId);
        return total != null && total > 0;
    }

}
