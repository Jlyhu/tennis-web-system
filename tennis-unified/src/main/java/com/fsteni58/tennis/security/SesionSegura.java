package com.fsteni58.tennis.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/** Crea la sesión segura en el servidor cuando el login es correcto. */
@Component
public class SesionSegura {

    private final SecurityContextRepository repositorio;

    public SesionSegura(SecurityContextRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Guarda al usuario en la sesión del servidor. Cambia el id de sesión si ya
     * existía una, para evitar que se reutilice una sesión anterior.
     */
    public void iniciar(UUID id, String nombre, String rol, UUID proveedorId,
                        HttpServletRequest req, HttpServletResponse res) {
        if (req.getSession(false) != null) {
            req.changeSessionId();
        }
        String rolBase = rol != null && rol.toLowerCase().startsWith("admin") ? "administrador"
                : String.valueOf(rol).toLowerCase();
        UsuarioAutenticado usuario = new UsuarioAutenticado(id, nombre, rolBase, proveedorId);

        SecurityContext contexto = SecurityContextHolder.createEmptyContext();
        contexto.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(
                usuario, null, List.of(new SimpleGrantedAuthority("ROLE_" + rolBase.toUpperCase()))));
        SecurityContextHolder.setContext(contexto);
        repositorio.saveContext(contexto, req, res);
    }
}