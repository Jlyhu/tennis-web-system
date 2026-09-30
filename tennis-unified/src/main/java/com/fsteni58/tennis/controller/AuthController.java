/*
 * Programa:     AuthController.java
 * Versión:      1.0
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Controlador REST de autenticación (Comprador US1, registro;
 *               Comprador US2, inicio de sesión; también usado para el
 *               registro de proveedores). Recibe las peticiones HTTP y
 *               delega toda la lógica de negocio a AuthService.
 */
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

    /**
     * Registra un usuario nuevo, comprador o proveedor (Comprador US1).
     * La validación de los campos del body la aplica @Valid sobre
     * RegistroRequest antes de que este método se ejecute; las reglas
     * adicionales de proveedor (empresa, NIT obligatorios) se validan
     * dentro de AuthService.registrar().
     * @param request datos de registro (nombre, correo, contraseña, rol,
     *                 y para proveedor: nombreEmpresa, nit, telefono, direccion)
     * @return 201 Created con el id generado y un mensaje de confirmación
     */
    @PostMapping("/register")
    public ResponseEntity<RegistroResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        UUID idGenerado = authService.registrar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegistroResponse(idGenerado, "Usuario registrado correctamente"));
    }

    /**
     * Autentica a un usuario existente (Comprador US2).
     * @param request credenciales (correo y contraseña)
     * @return 200 OK con el id y nombre del usuario si las credenciales son correctas
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = authService.iniciarSesion(request);
        return ResponseEntity.ok(
                new LoginResponse(usuario.getId(), usuario.getNombre(), "Inicio de sesión exitoso")
        );
    }
}