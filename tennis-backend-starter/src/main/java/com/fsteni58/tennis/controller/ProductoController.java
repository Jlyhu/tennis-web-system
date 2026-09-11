package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.ProductoRequest;
import com.fsteni58.tennis.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Proveedor US1: formulario para registrar inventario con datos completos
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoRequest request) {

        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "El precio no puede ser negativo"));
        }
        if (request.getStock() < 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "El stock no puede ser negativo"));
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre del producto es obligatorio"));
        }

        UUID idGenerado = productoRepository.guardar(request);

        return ResponseEntity.ok(Map.of("id", idGenerado, "mensaje", "Producto registrado correctamente"));
    }
}
