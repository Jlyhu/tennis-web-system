package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.model.TicketSoporte;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TicketSoporteRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TicketSoporte> mapper = (rs, rowNum) -> {
        TicketSoporte t = new TicketSoporte();
        t.setId((UUID) rs.getObject("id"));
        t.setCompradorId((UUID) rs.getObject("comprador_id"));
        t.setAsunto(rs.getString("asunto"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setRespuesta(rs.getString("respuesta"));
        t.setEstado(rs.getString("estado"));
        Timestamp creacion = rs.getTimestamp("fecha_creacion");
        if (creacion != null) t.setFechaCreacion(creacion.toInstant().atOffset(java.time.ZoneOffset.UTC));
        Timestamp respuesta = rs.getTimestamp("fecha_respuesta");
        if (respuesta != null) t.setFechaRespuesta(respuesta.toInstant().atOffset(java.time.ZoneOffset.UTC));
        return t;
    };

    public TicketSoporteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Comprador reporta un problema
    public UUID crear(UUID compradorId, String asunto, String descripcion) {
        String sql = """
            INSERT INTO tickets_soporte (comprador_id, asunto, descripcion, estado)
            VALUES (?, ?, ?, 'abierto')
            RETURNING id
            """;
        return jdbcTemplate.queryForObject(sql, UUID.class, compradorId, asunto, descripcion);
    }

    // Administrador ve todos los tickets (los más nuevos primero)
    public List<TicketSoporte> listarTodos() {
        String sql = "SELECT * FROM tickets_soporte ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, mapper);
    }

    // El comprador ve solo los suyos
    public List<TicketSoporte> listarPorComprador(UUID compradorId) {
        String sql = "SELECT * FROM tickets_soporte WHERE comprador_id = ? ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, mapper, compradorId);
    }

    public Optional<TicketSoporte> buscarPorId(UUID id) {
        String sql = "SELECT * FROM tickets_soporte WHERE id = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    // Administrador responde: guarda la respuesta y cambia el estado
    public int responder(UUID id, String respuesta) {
        String sql = """
            UPDATE tickets_soporte
            SET respuesta = ?, estado = 'respondido', fecha_respuesta = now()
            WHERE id = ?
            """;
        return jdbcTemplate.update(sql, respuesta, id);
    }

    public int cerrar(UUID id) {
        String sql = "UPDATE tickets_soporte SET estado = 'cerrado' WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
