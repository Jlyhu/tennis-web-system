package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.LoginRequest;
import com.fsteni58.tennis.dto.LoginResponse;
import com.fsteni58.tennis.dto.RegistroRequest;
import com.fsteni58.tennis.dto.RegistroResponse;
import com.fsteni58.tennis.model.Usuario;
import com.fsteni58.tennis.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistroResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        UUID idGenerado = authService.registrar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegistroResponse(idGenerado, "Usuario registrado correctamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = authService.iniciarSesion(request);
        return ResponseEntity.ok(
                new LoginResponse(usuario.getId(), usuario.getNombre(), "Inicio de sesión exitoso")
        );
    }
}