package com.fsteni58.tennis.dto;

import java.util.List;

public record OrdenPedidoRequest(
    String cliente,
    List<ItemPedidoDTO> items
) {}