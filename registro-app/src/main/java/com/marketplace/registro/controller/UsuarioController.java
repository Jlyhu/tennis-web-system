package com.marketplace.registro.controller;

import com.marketplace.registro.dto.RegistroRequest;
import com.marketplace.registro.dto.RegistroResponse;
import com.marketplace.registro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
// Abierto para que el frontend (html/js) pueda llamarlo desde cualquier origen,
// incluyendo cuando se prueba abriendo el html directamente desde el celular.
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<RegistroResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        RegistroResponse response = usuarioService.registrarComprador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
