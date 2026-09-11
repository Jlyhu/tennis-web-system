package com.fsteni58.tennis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ProveedorRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProveedorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Crea la fila de datos extra del proveedor (empresa, etc.), ligada a su usuario
    public UUID guardar(UUID usuarioId, String nombreEmpresa) {
        String sql = """
            INSERT INTO proveedores (usuario_id, nombre_empresa, activo)
            VALUES (?, ?, true)
            RETURNING id
            """;
        return jdbcTemplate.queryForObject(sql, UUID.class, usuarioId, nombreEmpresa);
    }
}
