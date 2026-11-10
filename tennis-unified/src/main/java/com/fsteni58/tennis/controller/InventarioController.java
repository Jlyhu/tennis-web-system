package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.MensajeResponse;
import com.fsteni58.tennis.dto.MovimientoRequest;
import com.fsteni58.tennis.dto.StockMinimoRequest;
import com.fsteni58.tennis.repository.InventarioRepository.Movimiento;
import com.fsteni58.tennis.repository.InventarioRepository.StockInventario;
import com.fsteni58.tennis.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    /** Stock actual, stock mínimo y alerta de cada producto del proveedor. */
    @GetMapping("/proveedor/{proveedorId}/stock")
    public ResponseEntity<List<StockInventario>> stock(@PathVariable UUID proveedorId) {
        return ResponseEntity.ok(inventarioService.listarStock(proveedorId));
    }

    /** Historial de movimientos de un producto. */
    @GetMapping("/productos/{id}/movimientos")
    public ResponseEntity<List<Movimiento>> movimientos(@PathVariable UUID id) {
        return ResponseEntity.ok(inventarioService.listarMovimientos(id));
    }

    /** Registra una entrada, salida o ajuste de inventario. */
    @PostMapping("/movimientos")
    public ResponseEntity<MensajeResponse> registrar(@Valid @RequestBody MovimientoRequest request) {
        inventarioService.registrarMovimiento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MensajeResponse("Movimiento registrado"));
    }

    /** Define el stock mínimo de un producto. */
    @PutMapping("/productos/{id}/stock-minimo")
    public ResponseEntity<MensajeResponse> stockMinimo(@PathVariable UUID id,
                                                       @Valid @RequestBody StockMinimoRequest request) {
        inventarioService.actualizarStockMinimo(id, request.stockMinimo());
        return ResponseEntity.ok(new MensajeResponse("Stock mínimo actualizado"));
    }
}