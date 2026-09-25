package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.ProductoRequest;
import com.fsteni58.tennis.dto.ProductoResponse;

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

    public List<ProductoResponse> obtenerTodos() {
    String sql = "SELECT id, nombre FROM productos";
    return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductoResponse(
            UUID.fromString(rs.getString("id")),
            rs.getString("nombre")
    ));
}

    public Optional<ProductoResponse> buscarPorId(UUID id) {
    String sql = "SELECT id, nombre FROM productos WHERE id = ?";
    List<ProductoResponse> resultados = jdbcTemplate.query(sql, (rs, rowNum) -> new ProductoResponse(
            UUID.fromString(rs.getString("id")),
            rs.getString("nombre")
    ), id);
    return resultados.stream().findFirst();
}
}
