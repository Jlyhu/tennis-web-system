/*
 * Programa:     ProductoCatalogoResponse.java
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  DTO de salida del catálogo público. Representa un producto
 *               tal como se muestra en catalogo.html: con su descripción,
 *               categoría, precio, stock e imagen. Es inmutable: sus
 *               valores se asignan solo por constructor.
 */
package com.fsteni58.tennis.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductoCatalogoResponse {

    /** Identificador único (UUID) del producto. */
    private UUID id;

    /** Nombre del producto. */
    private String nombre;

    /** Descripción del producto (puede ser null). */
    private String descripcion;

    /** Categoría del producto (raquetas, calzado o accesorios). */
    private String categoria;

    /** Precio del producto en pesos colombianos (COP). */
    private BigDecimal precio;

    /** Unidades disponibles en inventario. */
    private int stock;

    /** Ruta de la imagen dentro de la carpeta static (puede ser null). */
    private String imagenUrl;

    /**
     * Crea la representación de un producto para el catálogo.
     * @param id          identificador único del producto
     * @param nombre      nombre del producto
     * @param descripcion descripción del producto
     * @param categoria   categoría del producto
     * @param precio      precio en COP
     * @param stock       unidades disponibles
     * @param imagenUrl   ruta de la imagen
     */
    public ProductoCatalogoResponse(UUID id, String nombre, String descripcion,
                                    String categoria, BigDecimal precio, int stock, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
    }

    /**
     * Obtiene el identificador del producto.
     * @return id del producto
     */
    public UUID getId() { return id; }

    /**
     * Obtiene el nombre del producto.
     * @return nombre del producto
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene la descripción del producto.
     * @return descripción del producto (puede ser null)
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Obtiene la categoría del producto.
     * @return categoría del producto
     */
    public String getCategoria() { return categoria; }

    /**
     * Obtiene el precio del producto.
     * @return precio en COP
     */
    public BigDecimal getPrecio() { return precio; }

    /**
     * Obtiene el stock del producto.
     * @return unidades disponibles
     */
    public int getStock() { return stock; }

    /**
     * Obtiene la ruta de la imagen del producto.
     * @return ruta de la imagen (puede ser null)
     */
    public String getImagenUrl() { return imagenUrl; }
}