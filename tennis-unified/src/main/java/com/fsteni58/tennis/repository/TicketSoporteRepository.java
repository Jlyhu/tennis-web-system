/*
 * Programa:     TicketSoporteRepository.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Repositorio de acceso a datos del módulo de tickets de
 *               soporte (Comprador US8, generar PQRS; Administrador US10,
 *               canales de comunicación). Ejecuta las consultas SQL sobre
 *               la tabla tickets_soporte vía JdbcTemplate.
 */
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

    /** Convierte cada fila del ResultSet en un objeto TicketSoporte. */
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

    /**
     * Inserta un ticket nuevo en estado "abierto" (Comprador US8).
     * @param compradorId id del comprador que reporta el problema
     * @param asunto asunto breve del reporte
     * @param descripcion detalle del problema o consulta
     * @return id generado para el nuevo ticket
     */
    public UUID crear(UUID compradorId, String asunto, String descripcion) {
        String sql = """
            INSERT INTO tickets_soporte (comprador_id, asunto, descripcion, estado)
            VALUES (?, ?, ?, 'abierto')
            RETURNING id
            """;
        return jdbcTemplate.queryForObject(sql, UUID.class, compradorId, asunto, descripcion);
    }

    /**
     * Lista todos los tickets del sistema, del más reciente al más antiguo
     * (vista del Administrador).
     * @return lista completa de tickets
     */
    public List<TicketSoporte> listarTodos() {
        String sql = "SELECT * FROM tickets_soporte ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, mapper);
    }

    /**
     * Lista únicamente los tickets de un comprador, del más reciente al más antiguo.
     * @param compradorId id del comprador
     * @return lista de tickets de ese comprador
     */
    public List<TicketSoporte> listarPorComprador(UUID compradorId) {
        String sql = "SELECT * FROM tickets_soporte WHERE comprador_id = ? ORDER BY fecha_creacion DESC";
        return jdbcTemplate.query(sql, mapper, compradorId);
    }

    /**
     * Busca un ticket por su id.
     * @param id id del ticket
     * @return el ticket encontrado, o vacío si no existe
     */
    public Optional<TicketSoporte> buscarPorId(UUID id) {
        String sql = "SELECT * FROM tickets_soporte WHERE id = ?";
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    /**
     * Guarda la respuesta del administrador y cambia el estado a "respondido".
     * Nota (ver Test Case TC-002, Canales de Comunicación): esta consulta no
     * verifica el estado previo del ticket, por lo que responder un ticket
     * ya cerrado lo vuelve a dejar en "respondido" (lo reabre).
     * @param id id del ticket a responder
     * @param respuesta texto de la respuesta
     * @return cantidad de filas afectadas (0 o 1)
     */
    public int responder(UUID id, String respuesta) {
        String sql = """
            UPDATE tickets_soporte
            SET respuesta = ?, estado = 'respondido', fecha_respuesta = now()
            WHERE id = ?
            """;
        return jdbcTemplate.update(sql, respuesta, id);
    }

    /**
     * Cambia el estado de un ticket a "cerrado".
     * Nota (ver Test Case TC-002, Canales de Comunicación): esta consulta no
     * verifica que el ticket haya sido respondido antes, por lo que se puede
     * cerrar un ticket que nunca tuvo respuesta.
     * @param id id del ticket a cerrar
     * @return cantidad de filas afectadas (0 o 1)
     */
    public int cerrar(UUID id) {
        String sql = "UPDATE tickets_soporte SET estado = 'cerrado' WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}