package com.fsteni58.tennis.dto;

import com.fsteni58.tennis.model.EstadoOrden;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrdenPedidoResponse(
    UUID id,
    String cliente,
    LocalDate fecha,
    EstadoOrden estado,
    List<ItemPedidoDTO> items,
    BigDecimal total
) {}