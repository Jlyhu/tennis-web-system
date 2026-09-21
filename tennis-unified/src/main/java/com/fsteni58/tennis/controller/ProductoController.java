package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.ActualizarStockRequest;
import com.fsteni58.tennis.dto.ProductoRequest;
import com.fsteni58.tennis.dto.ProductoResponse;
import com.fsteni58.tennis.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        UUID idGenerado = productoService.registrarProducto(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ProductoResponse(idGenerado, "Producto registrado correctamente"));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable UUID id, @Valid @RequestBody ActualizarStockRequest request) {
        productoService.actualizarStock(id, request.getStock());
        return ResponseEntity.ok(Map.of("mensaje", "Stock actualizado correctamente"));
    }

}