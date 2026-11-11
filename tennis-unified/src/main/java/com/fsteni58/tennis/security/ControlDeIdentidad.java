package com.fsteni58.tennis.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.UUID;

/**
 * Antes de ejecutar un endpoint, revisa que los ids de la URL (usuarioId, compradorId,
 * proveedorId y el id de un producto) correspondan al usuario con sesión.
 */
@Component
public class ControlDeIdentidad implements HandlerInterceptor {

    private final AccesoIdentidad acceso;

    public ControlDeIdentidad(AccesoIdentidad acceso) {
        this.acceso = acceso;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        acceso.exigirUsuario(leerUuid(request.getParameter("usuarioId")));
        acceso.exigirUsuario(leerUuid(request.getParameter("compradorId")));

        Map<String, String> variables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (variables == null) return true;

        acceso.exigirProveedor(leerUuid(variables.get("proveedorId")));

        String uri = request.getRequestURI();
        boolean esDeProducto = uri.startsWith("/inventario/productos/")
                || (uri.startsWith("/productos/") && uri.endsWith("/stock"));
        if (esDeProducto) {
            acceso.exigirProducto(leerUuid(variables.get("id")));
        }
        return true;
    }

    /** Si el texto no es un UUID, se ignora: el controlador responde con su error de siempre. */
    private UUID leerUuid(String texto) {
        if (texto == null) return null;
        try {
            return UUID.fromString(texto);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}