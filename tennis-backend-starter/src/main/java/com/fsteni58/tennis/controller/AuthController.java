package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.LoginRequest;
import com.fsteni58.tennis.dto.RegistroRequest;
import com.fsteni58.tennis.model.Usuario;
import com.fsteni58.tennis.repository.ProveedorRepository;
import com.fsteni58.tennis.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepository usuarioRepository, ProveedorRepository proveedorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    // Comprador US1: registrarse en la plataforma
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {

        if (usuarioRepository.existeCorreo(request.getCorreo())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ese correo ya está registrado"));
        }

        Optional<UUID> rolId = usuarioRepository.buscarIdRolPorNombre(request.getRol());
        if (rolId.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rol inválido: " + request.getRol()));
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre());
        nuevo.setCorreo(request.getCorreo());
        nuevo.setContrasenaHash(passwordEncoder.encode(request.getContrasena())); // nunca se guarda en texto plano
        nuevo.setRolId(rolId.get());

        UUID idGenerado = usuarioRepository.guardar(nuevo);

        // Si se registró como proveedor, además creamos su fila de datos extra
        if ("proveedor".equals(request.getRol())) {
            proveedorRepository.guardar(idGenerado, request.getNombre() + " (empresa por definir)");
        }

        return ResponseEntity.ok(Map.of("id", idGenerado, "mensaje", "Usuario registrado correctamente"));
    }

    // Comprador US2: iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorCorreo(request.getCorreo());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasenaHash())) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
        }

        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "mensaje", "Inicio de sesión exitoso"
        ));
    }
}
