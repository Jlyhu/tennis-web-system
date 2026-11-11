package com.fsteni58.tennis.dto;

import com.fsteni58.tennis.util.PrecioFormatter;
import java.math.BigDecimal;
import java.util.UUID;

public class ConfirmarCompraResponse {

    private final UUID pedidoId;
    private final BigDecimal total;
    private final String totalCop;
    private final String mensaje;

    public ConfirmarCompraResponse(UUID pedidoId, BigDecimal total, String mensaje) {
        this.pedidoId = pedidoId;
        this.total = total;
        this.totalCop = PrecioFormatter.aCop(total);
        this.mensaje = mensaje;
    }

    public UUID getPedidoId() { return pedidoId; }
    public BigDecimal getTotal() { return total; }
    public String getTotalCop() { return totalCop; }
    public String getMensaje() { return mensaje; }
}