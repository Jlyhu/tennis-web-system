package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.ProductoRequest;
import com.fsteni58.tennis.repository.ProductoRepository;
import com.fsteni58.tennis.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

// Proveedor US1: lógica de negocio del registro de inventario
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

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
}