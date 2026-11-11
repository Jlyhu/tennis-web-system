
package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.CompraResponse;
import com.fsteni58.tennis.service.HistorialCompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/compras")
public class HistorialCompraController {

    private final HistorialCompraService service;

    public HistorialCompraController(HistorialCompraService service) {
        this.service = service;
    }

    @GetMapping("/mias")
    public ResponseEntity<List<CompraResponse>> consultar(
            @RequestAttribute("usuarioId") UUID compradorId) {

        return ResponseEntity.ok(
            service.consultarHistorial(compradorId)
        );
    }
}
