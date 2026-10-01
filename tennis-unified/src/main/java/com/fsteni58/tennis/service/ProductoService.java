/*
 * Programa:     ProductoService.java
 * Versión:      1.0
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de negocio del módulo de inventario (Proveedor US1,
 *               registro de productos; Proveedor US5, actualización de stock).
 *               Ver ProductoServiceTest.java para las pruebas unitarias
 *               correspondientes a actualizarStock (PU-001).
 */
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.ProductoRequest;
import com.fsteni58.tennis.dto.ProductoResponse;
import com.fsteni58.tennis.exception.ProductoNoEncontradoException;
import com.fsteni58.tennis.repository.ProductoRepository;
import com.fsteni58.tennis.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Lógica de negocio del módulo de inventario (Proveedor US1, registro de
 * productos; Proveedor US5, actualización de stock).
 * Ver ProductoServiceTest para las pruebas unitarias de actualizarStock (PU-001).
 *
 * @author Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *         Moreno Cortes, Orosco Quemba
 * @version 1.0
 * @since 29/09/2026
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Registra un producto nuevo en el inventario de un proveedor (Proveedor US1).
     * Valida que el precio y el stock no sean negativos, que el nombre no esté
     * vacío, y que el proveedor exista y esté activo.
     * @param request datos del producto a registrar
     * @return id generado para el nuevo producto
     * @throws IllegalArgumentException si algún dato es inválido o el proveedor no existe/no está activo
     */
    public UUID registrarProducto(ProductoRequest request) {
        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (request.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (request.getProveedorId() == null) {
            throw new IllegalArgumentException("El proveedor es obligatorio");
        }
        if (!proveedorRepository.existeActivo(request.getProveedorId())) {
            throw new IllegalArgumentException("El proveedor indicado no existe o no está activo");
        }

        return productoRepository.guardar(request);
    }

    /**
     * Actualiza el stock de un producto existente (Proveedor US5).
     * Solo acepta valores enteros no negativos, y el producto debe existir
     * y estar activo. Ver Test Case TC-001 (Actualizar Stock) para la
     * evidencia manual de este comportamiento, y TC-002 para el defecto
     * detectado en la validación del formato del id.
     * @param productoId id del producto a actualizar
     * @param nuevoStock nuevo valor de stock, debe ser >= 0
     * @throws IllegalArgumentException si nuevoStock es negativo
     * @throws ProductoNoEncontradoException si no existe un producto activo con ese id
     */
    public void actualizarStock(UUID productoId, int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (!productoRepository.existeProducto(productoId)) {
            throw new ProductoNoEncontradoException("No existe un producto activo con id " + productoId);
        }
        productoRepository.actualizarStock(productoId, nuevoStock);
    }

    /**
     * Retorna todos los productos registrados.
     * @return lista de productos
     */
    public List<ProductoResponse> obtenerTodos() {
        return productoRepository.obtenerTodos();
    }

    /**
     * Busca un producto por su id.
     * @param id id del producto
     * @return el producto encontrado
     * @throws ProductoNoEncontradoException si no existe un producto con ese id
     */
    public ProductoResponse buscarPorId(UUID id) {
        return productoRepository.buscarPorId(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("No se encontró el producto con el ID especificado: " + id));
    }
}