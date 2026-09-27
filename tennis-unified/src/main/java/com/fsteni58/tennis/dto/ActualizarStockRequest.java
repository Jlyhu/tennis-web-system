package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ActualizarStockRequest {

    @NotNull(message = "El stock nuevo es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
