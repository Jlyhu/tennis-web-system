package com.fsteni58.tennis.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/** Comprueba que el id que llega en la petición sea del usuario con sesión. */
@Component
public class AccesoIdentidad {

    private final JdbcTemplate jdbcTemplate;

    public AccesoIdentidad(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UsuarioAutenticado actual() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a != null && a.getPrincipal() instanceof UsuarioAutenticado u) {
            return u;
        }
        throw new AccessDeniedException("Inicia sesión para continuar.");
    }

    /** El usuarioId / compradorId de la petición debe ser el de la sesión. */
    public void exigirUsuario(UUID usuarioId) {
        if (usuarioId != null && !usuarioId.equals(actual().id())) {
            throw new AccessDeniedException("No puedes actuar a nombre de otro usuario.");
        }
    }

    /** El proveedorId de la petición debe ser el del proveedor con sesión. */
    public void exigirProveedor(UUID proveedorId) {
        if (proveedorId != null && !proveedorId.equals(actual().proveedorId())) {
            throw new AccessDeniedException("Ese inventario no pertenece a tu cuenta de proveedor.");
        }
    }

    /** El producto debe ser del proveedor con sesión. Si no existe, se deja pasar (el servicio responde 404). */
    public void exigirProducto(UUID productoId) {
        if (productoId == null) return;
        List<UUID> duenos = jdbcTemplate.query("SELECT proveedor_id FROM productos WHERE id = ?",
                (rs, n) -> (UUID) rs.getObject("proveedor_id"), productoId);
        if (!duenos.isEmpty() && !duenos.get(0).equals(actual().proveedorId())) {
            throw new AccessDeniedException("Ese producto no pertenece a tu cuenta de proveedor.");
        }
    }
}