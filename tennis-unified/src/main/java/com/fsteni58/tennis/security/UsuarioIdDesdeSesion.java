package com.fsteni58.tennis.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Pone en la petición el usuarioId de la sesión, que es lo que leen los controladores de compras y promociones. */
@Component
public class UsuarioIdDesdeSesion implements HandlerInterceptor {

    private final AccesoIdentidad acceso;

    public UsuarioIdDesdeSesion(AccesoIdentidad acceso) {
        this.acceso = acceso;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("usuarioId", acceso.actual().id());
        return true;
    }
}