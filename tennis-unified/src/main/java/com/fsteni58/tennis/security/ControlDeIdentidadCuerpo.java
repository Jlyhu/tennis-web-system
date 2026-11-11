package com.fsteni58.tennis.security;

import com.fsteni58.tennis.dto.CrearTicketRequest;
import com.fsteni58.tennis.dto.MovimientoRequest;
import com.fsteni58.tennis.dto.ProductoRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * Revisa los ids que vienen dentro del cuerpo de la petición: el proveedor al crear un producto,
 * el producto de un movimiento de inventario y el comprador al crear un ticket.
 */
@ControllerAdvice
public class ControlDeIdentidadCuerpo extends RequestBodyAdviceAdapter {

    private final AccesoIdentidad acceso;

    public ControlDeIdentidadCuerpo(AccesoIdentidad acceso) {
        this.acceso = acceso;
    }

    @Override
    public boolean supports(MethodParameter parametro, Type tipo, Class<? extends HttpMessageConverter<?>> conversor) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object cuerpo, HttpInputMessage mensaje, MethodParameter parametro,
                                Type tipo, Class<? extends HttpMessageConverter<?>> conversor) {
        if (cuerpo instanceof ProductoRequest p) {
            acceso.exigirProveedor(p.getProveedorId());
        } else if (cuerpo instanceof MovimientoRequest m) {
            acceso.exigirProducto(m.productoId());
        } else if (cuerpo instanceof CrearTicketRequest t) {
            acceso.exigirUsuario(t.getCompradorId());
        }
        return cuerpo;
    }
}