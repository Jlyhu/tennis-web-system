package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.LoginRequest;
import com.fsteni58.tennis.dto.RegistroRequest;
import com.fsteni58.tennis.exception.CorreoDuplicadoException;
import com.fsteni58.tennis.exception.CredencialesInvalidasException;
import com.fsteni58.tennis.model.Usuario;
import com.fsteni58.tennis.repository.ProveedorRepository;
import com.fsteni58.tennis.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

// Toda la lógica de negocio del registro y el login vive aquí,
// separada del controlador (que solo recibe la petición HTTP).
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, ProveedorRepository proveedorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    // Comprador US1
    public UUID registrar(RegistroRequest request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existeCorreo(correoNormalizado)) {
            throw new CorreoDuplicadoException("Ese correo ya está registrado");
        }

        UUID rolId = usuarioRepository.buscarIdRolPorNombre(request.getRol())
            .orElseThrow(() -> new IllegalArgumentException("Rol inválido: " + request.getRol()));

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre().trim());
        nuevo.setCorreo(correoNormalizado);
        nuevo.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
        nuevo.setRolId(rolId);

        UUID idGenerado = usuarioRepository.guardar(nuevo);

        if ("proveedor".equals(request.getRol())) {
            proveedorRepository.guardar(idGenerado, request.getNombre() + " (empresa por definir)");
        }

        return idGenerado;
    }

    // Comprador US2
    public Usuario iniciarSesion(LoginRequest request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        Usuario usuario = usuarioRepository.buscarPorCorreo(correoNormalizado)
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasenaHash())) {
            throw new CredencialesInvalidasException("Contraseña incorrecta");
        }

        return usuario;
    }
}
