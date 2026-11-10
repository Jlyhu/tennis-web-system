package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.LiquidacionRequest;
import com.fsteni58.tennis.dto.MensajeResponse;
import com.fsteni58.tennis.repository.LiquidacionRepository.*;
import com.fsteni58.tennis.service.LiquidacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin/liquidaciones")
public class LiquidacionController {

    private final LiquidacionService liquidacionService;

    public LiquidacionController(LiquidacionService liquidacionService) {
        this.liquidacionService = liquidacionService;
    }

    @GetMapping
    public ResponseEntity<List<Liquidacion>> listar() {
        return ResponseEntity.ok(liquidacionService.listar());
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<List<Linea>> detalle(@PathVariable UUID id) {
        return ResponseEntity.ok(liquidacionService.detalle(id));
    }

    @GetMapping("/proveedores")
    public ResponseEntity<List<ProveedorOpcion>> proveedores() {
        return ResponseEntity.ok(liquidacionService.proveedores());
    }

    @GetMapping("/motivos-salida")
    public ResponseEntity<List<MotivoSalida>> motivos() {
        return ResponseEntity.ok(liquidacionService.motivos());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody LiquidacionRequest request) {
        UUID id = liquidacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", id, "mensaje", "Liquidación generada"));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<MensajeResponse> pagar(@PathVariable UUID id) {
        liquidacionService.marcarPagada(id);
        return ResponseEntity.ok(new MensajeResponse("Liquidación marcada como pagada"));
    }
}