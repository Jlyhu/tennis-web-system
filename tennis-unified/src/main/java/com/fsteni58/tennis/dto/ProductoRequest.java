/*
 * Programa:     ProductoRequest.java
 * Versión:      1.0
 * Fecha:        30/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de entrada para registrar un producto. Transporta el
 *               proveedor, nombre, descripción, categoría, precio y stock
 *               desde el body de la petición. Las restricciones de
 *               validación (campos obligatorios y valores numéricos
 *               positivos) se aplican con @Valid en el controlador antes
 *               de llegar a la capa de servicio.
 */
package com.fsteni58.tennis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductoRequest {

    /** Identificador único (UUID) del proveedor dueño del producto. Obligatorio. */
    @NotNull(message = "El proveedor es obligatorio")
    private UUID proveedorId;

    /** Nombre del producto. Obligatorio, no puede estar vacío. */
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    /** Descripción del producto. Opcional. */
    private String descripcion;

    /** Categoría del producto. Obligatoria, no puede estar vacía. */
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    /** Precio del producto. Obligatorio y mayor a cero. */
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private BigDecimal precio;

    /** Unidades disponibles del producto. Obligatorio y positivo (@Positive). */
    @NotNull(message = "El stock es obligatorio")
    @Positive(message = "El stock debe ser mayor o igual a cero")
    private Integer stock;

    /**
     * Obtiene el identificador del proveedor.
     * @return id del proveedor dueño del producto
     */
    public UUID getProveedorId() { return proveedorId; }

    /**
     * Asigna el identificador del proveedor.
     * @param proveedorId id del proveedor dueño del producto
     */
    public void setProveedorId(UUID proveedorId) { this.proveedorId = proveedorId; }

    /**
     * Obtiene el nombre del producto.
     * @return nombre del producto
     */
    public String getNombre() { return nombre; }

    /**
     * Asigna el nombre del producto.
     * @param nombre nombre del producto
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene la descripción del producto.
     * @return descripción del producto (puede ser null)
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Asigna la descripción del producto.
     * @param descripcion descripción del producto
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * Obtiene la categoría del producto.
     * @return categoría del producto
     */
    public String getCategoria() { return categoria; }

    /**
     * Asigna la categoría del producto.
     * @param categoria categoría del producto
     */
    public void setCategoria(String categoria) { this.categoria = categoria; }

    /**
     * Obtiene el precio del producto.
     * @return precio del producto
     */
    public BigDecimal getPrecio() { return precio; }

    /**
     * Asigna el precio del producto.
     * @param precio precio del producto
     */
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    /**
     * Obtiene el stock del producto.
     * @return unidades disponibles del producto
     */
    public Integer getStock() { return stock; }

    /**
     * Asigna el stock del producto.
     * @param stock unidades disponibles del producto
     */
    public void setStock(Integer stock) { this.stock = stock; }
}