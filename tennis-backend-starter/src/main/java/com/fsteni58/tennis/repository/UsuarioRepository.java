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

    // Busca el id de un rol por su nombre (ej: "comprador" -> su UUID en la tabla roles)
    public Optional<UUID> buscarIdRolPorNombre(String nombreRol) {
        String sql = "SELECT id FROM roles WHERE nombre_rol = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of((UUID) rs.getObject("id"));
            }
            return Optional.empty();
        }, nombreRol);
    }

    // Revisa si ya existe un usuario con ese correo (para no duplicar)
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, correo);
        return total != null && total > 0;
    }

    // Inserta un usuario nuevo y devuelve su id generado
    public UUID guardar(Usuario usuario) {
        String sql = """
            INSERT INTO usuarios (nombre, correo, contrasena_hash, rol_id, activo)
            VALUES (?, ?, ?, ?, true)
            RETURNING id
            """;
        return jdbcTemplate.queryForObject(sql, UUID.class,
                usuario.getNombre(), usuario.getCorreo(), usuario.getContrasenaHash(), usuario.getRolId());
    }

    // Busca un usuario por correo (para el login)
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
