/*
 * Programa:     ProductoController.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Controlador REST del módulo de inventario (Proveedor US1,
 *               registro de productos; Proveedor US5, actualización de
 *               stock). Recibe las peticiones HTTP y delega toda la lógica
 *               de negocio a ProductoService. Ver Test Case TC-001 y TC-002
 *               (Actualizar Stock) para la evidencia manual de este endpoint.
 */
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

    /**
     * Registra un producto nuevo en el inventario de un proveedor (Proveedor US1).
     * La validación de los campos del body la aplica @Valid sobre
     * ProductoRequest antes de que este método se ejecute.
     * @param request datos del producto (proveedorId, nombre, descripcion, categoria, precio, stock)
     * @return 201 Created con el id generado y un mensaje de confirmación
     */
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        UUID idGenerado = productoService.registrarProducto(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ProductoResponse(idGenerado, "Producto registrado correctamente"));
    }

    /**
     * Actualiza el stock de un producto existente (Proveedor US5).
     * @param id id del producto, tomado de la ruta
     * @param request nuevo valor de stock, validado como entero no negativo por ActualizarStockRequest
     * @return 200 OK con un mensaje de confirmación
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable UUID id, @Valid @RequestBody ActualizarStockRequest request) {
        productoService.actualizarStock(id, request.getStock());
        return ResponseEntity.ok(Map.of("mensaje", "Stock actualizado correctamente"));
    }

}