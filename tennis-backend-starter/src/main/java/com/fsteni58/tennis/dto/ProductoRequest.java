package com.fsteni58.tennis.dto;

import java.math.BigDecimal;
import java.util.UUID;

// Lo que llega desde el formulario de inventario del proveedor (Proveedor US1)
public class ProductoRequest {
    private UUID proveedorId;
    private String nombre;
    private String descripcion;
    private String categoria;
    private BigDecimal precio;
    private int stock;

    public UUID getProveedorId() { return proveedorId; }
    public void setProveedorId(UUID proveedorId) { this.proveedorId = proveedorId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
