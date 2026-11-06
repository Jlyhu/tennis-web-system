/*
 * Programa:     CarritoItemResponse.java
 * Versión:      1.0
 * Fecha:        06/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida con un producto dentro del carrito: sus
 *               datos, la cantidad elegida y el subtotal (precio por
 *               cantidad). Es inmutable: sus valores se asignan solo por
 *               constructor.
 */
package com.fsteni58.tennis.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CarritoItemResponse {

    /** Identificador único (UUID) del producto. */
    private UUID productoId;

    /** Nombre del producto. */
    private String nombre;

    /** Ruta de la imagen del producto (puede ser null). */
    private String imagenUrl;

    /** Precio de una unidad, en COP. */
    private BigDecimal precioUnitario;

    /** Unidades de este producto en el carrito. */
    private int cantidad;

    /** Unidades disponibles en inventario. */
    private int stock;

    /** Precio unitario por cantidad. */
    private BigDecimal subtotal;

    /**
     * Crea un ítem del carrito y calcula su subtotal.
     * @param productoId     id del producto
     * @param nombre         nombre del producto
     * @param imagenUrl      ruta de la imagen
     * @param precioUnitario precio de una unidad
     * @param cantidad       unidades en el carrito
     * @param stock          unidades disponibles en inventario
     */
    public CarritoItemResponse(UUID productoId, String nombre, String imagenUrl,
                               BigDecimal precioUnitario, int cantidad, int stock) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.stock = stock;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * Obtiene el id del producto.
     * @return id del producto
     */
    public UUID getProductoId() { return productoId; }

    /**
     * Obtiene el nombre del producto.
     * @return nombre del producto
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene la ruta de la imagen.
     * @return ruta de la imagen (puede ser null)
     */
    public String getImagenUrl() { return imagenUrl; }

    /**
     * Obtiene el precio de una unidad.
     * @return precio unitario en COP
     */
    public BigDecimal getPrecioUnitario() { return precioUnitario; }

    /**
     * Obtiene la cantidad en el carrito.
     * @return unidades en el carrito
     */
    public int getCantidad() { return cantidad; }

    /**
     * Obtiene el stock disponible.
     * @return unidades en inventario
     */
    public int getStock() { return stock; }

    /**
     * Obtiene el subtotal del ítem.
     * @return precio unitario por cantidad
     */
    public BigDecimal getSubtotal() { return subtotal; }
}