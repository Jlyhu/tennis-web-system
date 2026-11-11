
package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.PromocionResponse;
import com.fsteni58.tennis.service.PromocionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/promociones")
public class PromocionCompradorController {

    private final PromocionService service;

    public PromocionCompradorController(PromocionService service) {
        this.service = service;
    }

    @GetMapping("/recomendadas")
    public ResponseEntity<List<PromocionResponse>> recomendadas(
            @RequestAttribute("usuarioId") UUID compradorId) {

        return ResponseEntity.ok(
            service.obtenerParaComprador(compradorId)
        );
    }
}
