/*
 * Programa:     AuthService.java
 * Versión:      1.1
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de negocio del registro y el inicio de sesión
 *               (Comprador US1, registro; Comprador US2, inicio de sesión;
 *               y el registro de proveedores). Separada del controlador,
 *               que solo recibe la petición HTTP.
 * Cambios v1.1: registrar() ahora valida y guarda los datos reales de
 *               empresa del proveedor (nombreEmpresa, nit, telefono,
 *               direccion) en vez del texto de relleno que se usaba antes.
 */
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

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, ProveedorRepository proveedorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    /**
     * Registra un usuario nuevo, comprador o proveedor (Comprador US1,
     * Proveedor US1). Si el rol es "proveedor", exige además nombre de
     * empresa y NIT, y crea también el registro correspondiente en la
     * tabla proveedores.
     * @param request datos de registro
     * @return id generado para el nuevo usuario
     * @throws CorreoDuplicadoException si el correo ya está registrado
     * @throws IllegalArgumentException si el rol es inválido, o si es
     *         proveedor y faltan nombreEmpresa o nit
     */
    public UUID registrar(RegistroRequest request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existeCorreo(correoNormalizado)) {
            throw new CorreoDuplicadoException("Ese correo ya está registrado");
        }

        boolean esProveedor = "proveedor".equals(request.getRol());

        // Un proveedor sin nombre de empresa o sin NIT no queda registrado como
        // proveedor válido — antes esto se rellenaba con un texto de relleno.
        if (esProveedor) {
            if (request.getNombreEmpresa() == null || request.getNombreEmpresa().isBlank()) {
                throw new IllegalArgumentException("El nombre de la empresa es obligatorio para proveedores");
            }
            if (request.getNit() == null || request.getNit().isBlank()) {
                throw new IllegalArgumentException("El NIT es obligatorio para proveedores");
            }
        }

        UUID rolId = usuarioRepository.buscarIdRolPorNombre(request.getRol())
            .orElseThrow(() -> new IllegalArgumentException("Rol inválido: " + request.getRol()));

        Usuario nuevo = new Usuario();
        nuevo.setNombre(request.getNombre().trim());
        nuevo.setCorreo(correoNormalizado);
        nuevo.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
        nuevo.setRolId(rolId);

        UUID idGenerado = usuarioRepository.guardar(nuevo);

        if (esProveedor) {
            proveedorRepository.guardar(
                idGenerado,
                request.getNombreEmpresa().trim(),
                request.getNit().trim(),
                request.getTelefono() == null ? null : request.getTelefono().trim(),
                request.getDireccion() == null ? null : request.getDireccion().trim()
            );
        }

        return idGenerado;
    }

    /**
     * Autentica a un usuario existente comparando la contraseña con su hash
     * guardado (Comprador US2).
     * @param request credenciales (correo y contraseña)
     * @return el usuario autenticado
     * @throws CredencialesInvalidasException si el usuario no existe o la contraseña no coincide
     */
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