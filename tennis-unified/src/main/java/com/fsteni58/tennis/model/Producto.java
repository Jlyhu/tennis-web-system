/*

Programa:     Producto.java
Versión:      1.0
Fecha:        30/09/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Clase modelo que representa la entidad Producto en el 
              catálogo del sistema. Contiene los detalles de los artículos 
              (tenis/calzado deportivo) ofrecidos, vinculándolos a su 
              proveedor e incluyendo datos como precio, stock y categoría.
*/

package com.fsteni58.tennis.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Modelo de dominio que representa a un producto disponible en la tienda.
 * Se encarga de mapear los datos del artículo, permitiendo la gestión del inventario,
 * la clasificación por categorías y la asociación directa con el proveedor que lo publica.
 */
public class Producto {
    private UUID id;
    private UUID proveedorId;
    private String nombre;
    private String descripcion;
    private String categoria;
    private BigDecimal precio;
    private int stock;
    private boolean activo;

    /**
     * Constructor por defecto.
     * Requerido habitualmente para procesos de serialización/deserialización 
     * o por frameworks de mapeo objeto-relacional (ORM).
     */
    public Producto() {}

    /**
     * Obtiene el identificador único del producto.
     * 
     * @return id del producto
     */
    public UUID getId() { return id; }

    /**
     * Establece el identificador único del producto.
     * 
     * @param id nuevo identificador UUID
     */
    public void setId(UUID id) { this.id = id; }

    /**
     * Obtiene el identificador del proveedor dueño de este producto.
     * 
     * @return id del proveedor
     */
    public UUID getProveedorId() { return proveedorId; }

    /**
     * Establece el identificador del proveedor que oferta el producto.
     * 
     * @param proveedorId UUID del proveedor
     */
    public void setProveedorId(UUID proveedorId) { this.proveedorId = proveedorId; }

    /**
     * Obtiene el nombre o título del producto.
     * 
     * @return nombre del producto
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre del producto.
     * 
     * @param nombre nuevo nombre a asignar
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene la descripción detallada del producto.
     * 
     * @return descripción del artículo
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Establece la descripción del producto.
     * 
     * @param descripcion nueva descripción detallada
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * Obtiene la categoría a la que pertenece el producto.
     * 
     * @return categoría del producto
     */
    public String getCategoria() { return categoria; }

    /**
     * Establece la categoría del producto (ej. Running, Casual, etc.).
     * 
     * @param categoria nueva categoría a asignar
     */
    public void setCategoria(String categoria) { this.categoria = categoria; }

    /**
     * Obtiene el precio de venta del producto.
     * 
     * @return precio actual usando BigDecimal para precisión monetaria
     */
    public BigDecimal getPrecio() { return precio; }

    /**
     * Establece el precio de venta del producto.
     * 
     * @param precio nuevo valor del producto
     */
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    /**
     * Obtiene la cantidad de unidades disponibles en inventario.
     * 
     * @return cantidad en stock
     */
    public int getStock() { return stock; }

    /**
     * Establece la cantidad de unidades disponibles en inventario.
     * 
     * @param stock nueva cantidad de stock
     */
    public void setStock(int stock) { this.stock = stock; }

    /**
     * Verifica si el producto está activo y visible para la venta.
     * 
     * @return true si el producto está activo, false si está oculto/inactivo
     */
    public boolean isActivo() { return activo; }

    /**
     * Establece el estado de disponibilidad del producto.
     * 
     * @param activo true para marcar el producto como disponible, false para desactivarlo
     */
    public void setActivo(boolean activo) { this.activo = activo; }
}