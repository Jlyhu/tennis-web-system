/*
 * Programa:     UsuarioRepository.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Repositorio de acceso a datos para el registro y la
 *               autenticación de usuarios (Comprador US1, registro;
 *               Comprador US2, inicio de sesión). Ejecuta las consultas
 *               SQL sobre las tablas usuarios y roles vía JdbcTemplate.
 */
package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Busca el id de un rol a partir de su nombre (ej. "comprador", "proveedor").
     * @param nombreRol nombre del rol a buscar
     * @return el id del rol, o vacío si no existe un rol con ese nombre
     */
    public Optional<UUID> buscarIdRolPorNombre(String nombreRol) {
        String sql = "SELECT id FROM roles WHERE nombre_rol = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) return Optional.of((UUID) rs.getObject("id"));
            return Optional.empty();
        }, nombreRol);
    }

    /**
     * Verifica si ya existe un usuario registrado con ese correo.
     * @param correo correo a verificar
     * @return true si el correo ya está en uso, false en caso contrario
     */
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, correo);
        return total != null && total > 0;
    }

    /**
     * Inserta un usuario nuevo, activo por defecto.
     * @param usuario datos del usuario a registrar (la contraseña ya debe venir hasheada)
     * @return id generado para el nuevo usuario
     */
    public UUID guardar(Usuario usuario) {
        String sql = """
            INSERT INTO usuarios (nombre, correo, contrasena_hash, rol_id, activo)
            VALUES (?, ?, ?, ?, true)
            RETURNING id
            """;
        return jdbcTemplate.queryForObject(sql, UUID.class,
                usuario.getNombre(), usuario.getCorreo(), usuario.getContrasenaHash(), usuario.getRolId());
    }

    /**
     * Busca un usuario activo por su correo (usado al iniciar sesión).
     * @param correo correo del usuario
     * @return el usuario encontrado, o vacío si no existe o está inactivo
     */
    public Optional<Usuario> buscarPorCorreo(String correo) {
        String sql = "SELECT id, nombre, correo, contrasena_hash, rol_id, activo FROM usuarios WHERE correo = ? AND activo = true";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId((UUID) rs.getObject("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasenaHash(rs.getString("contrasena_hash"));
                u.setRolId((UUID) rs.getObject("rol_id"));
                u.setActivo(rs.getBoolean("activo"));
                return Optional.of(u);
            }
            return Optional.empty();
        }, correo);
    }
}