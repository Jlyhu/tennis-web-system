package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.model.Cliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ClienteRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Cliente> mapper = (rs, rowNum) -> new Cliente(
            (UUID) rs.getObject("id"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getBoolean("activo")
    );

    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cliente> listarTodos() {
        String sql = """
            SELECT u.id, u.nombre, u.correo, u.activo
            FROM usuarios u
            JOIN roles r ON u.rol_id = r.id
            WHERE r.nombre_rol = 'comprador'
            ORDER BY u.nombre
            """;
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Cliente> buscarPorId(UUID id) {
        String sql = """
            SELECT u.id, u.nombre, u.correo, u.activo
            FROM usuarios u
            JOIN roles r ON u.rol_id = r.id
            WHERE r.nombre_rol = 'comprador' AND u.id = ?
            """;
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    public int actualizar(UUID id, String nombre, String correo) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ? WHERE id = ?";
        return jdbcTemplate.update(sql, nombre, correo, id);
    }

    public int desactivar(UUID id) {
        String sql = "UPDATE usuarios SET activo = false WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
