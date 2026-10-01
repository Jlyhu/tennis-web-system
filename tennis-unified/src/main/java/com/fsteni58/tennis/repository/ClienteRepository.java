/*
 * Programa:     ClienteRepository.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Repositorio de acceso a datos para la gestión de clientes
 *               (Administrador US5). Consulta y modifica usuarios cuyo rol
 *               es "comprador" sobre la tabla usuarios, vía JdbcTemplate.
 */
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

    /** Convierte cada fila del ResultSet en un objeto Cliente. */
    private final RowMapper<Cliente> mapper = (rs, rowNum) -> new Cliente(
            (UUID) rs.getObject("id"),
            rs.getString("nombre"),
            rs.getString("correo"),
            rs.getBoolean("activo")
    );

    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todos los usuarios cuyo rol es "comprador".
     * @return lista de clientes, ordenada por nombre
     */
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

    /**
     * Busca un cliente (usuario con rol comprador) por su id.
     * @param id id del cliente
     * @return el cliente encontrado, o vacío si no existe o no es comprador
     */
    public Optional<Cliente> buscarPorId(UUID id) {
        String sql = """
            SELECT u.id, u.nombre, u.correo, u.activo
            FROM usuarios u
            JOIN roles r ON u.rol_id = r.id
            WHERE r.nombre_rol = 'comprador' AND u.id = ?
            """;
        return jdbcTemplate.query(sql, mapper, id).stream().findFirst();
    }

    /**
     * Actualiza el nombre y correo de un cliente.
     * @param id id del cliente
     * @param nombre nuevo nombre
     * @param correo nuevo correo
     * @return cantidad de filas afectadas (0 o 1)
     */
    public int actualizar(UUID id, String nombre, String correo) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ? WHERE id = ?";
        return jdbcTemplate.update(sql, nombre, correo, id);
    }

    /**
     * Desactiva un cliente (borrado lógico).
     * @param id id del cliente
     * @return cantidad de filas afectadas (0 o 1)
     */
    public int desactivar(UUID id) {
        String sql = "UPDATE usuarios SET activo = false WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}