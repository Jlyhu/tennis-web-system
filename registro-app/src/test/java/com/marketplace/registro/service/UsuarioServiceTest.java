package com.marketplace.registro.service;

import com.marketplace.registro.dto.RegistroRequest;
import com.marketplace.registro.dto.RegistroResponse;
import com.marketplace.registro.exception.CamposInvalidosException;
import com.marketplace.registro.exception.CorreoDuplicadoException;
import com.marketplace.registro.model.Rol;
import com.marketplace.registro.model.Usuario;
import com.marketplace.registro.repository.RolRepository;
import com.marketplace.registro.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    private RegistroRequest requestValido;
    private Rol rolComprador;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository, rolRepository, passwordEncoder);

        requestValido = new RegistroRequest();
        requestValido.setNombre("Laura Gómez");
        requestValido.setCorreo("laura@example.com");
        requestValido.setContrasena("clave12345");

        rolComprador = new Rol();
        rolComprador.setId(UUID.randomUUID());
        rolComprador.setNombreRol("comprador");
    }

    // ---------- CASO VÁLIDO ----------
    @Test
    void registrarComprador_casoValido_guardaUsuarioConRolCompradorYContrasenaHasheada() {
        when(usuarioRepository.existsByCorreoIgnoreCase("laura@example.com")).thenReturn(false);
        when(rolRepository.findByNombreRol("comprador")).thenReturn(Optional.of(rolComprador));
        when(passwordEncoder.encode("clave12345")).thenReturn("$2a$10$hashSimulado");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocacion -> {
            Usuario u = invocacion.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        RegistroResponse response = usuarioService.registrarComprador(requestValido);

        assertNotNull(response.getId());
        assertEquals("laura@example.com", response.getCorreo());
        assertEquals("comprador", response.getRol());

        // La contraseña jamás debe guardarse en texto plano
        verify(usuarioRepository).save(argThatUsuarioConHash("$2a$10$hashSimulado"));
    }

    // ---------- CORREO DUPLICADO ----------
    @Test
    void registrarComprador_correoDuplicado_lanzaCorreoDuplicadoExceptionYNoGuarda() {
        when(usuarioRepository.existsByCorreoIgnoreCase("laura@example.com")).thenReturn(true);

        assertThrows(CorreoDuplicadoException.class,
                () -> usuarioService.registrarComprador(requestValido));

        verify(usuarioRepository, never()).save(any());
    }

    // ---------- CAMPOS FALTANTES ----------
    @Test
    void registrarComprador_nombreFaltante_lanzaCamposInvalidosException() {
        requestValido.setNombre("   ");

        assertThrows(CamposInvalidosException.class,
                () -> usuarioService.registrarComprador(requestValido));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarComprador_correoFaltante_lanzaCamposInvalidosException() {
        requestValido.setCorreo("");

        assertThrows(CamposInvalidosException.class,
                () -> usuarioService.registrarComprador(requestValido));
    }

    @Test
    void registrarComprador_correoConFormatoInvalido_lanzaCamposInvalidosException() {
        requestValido.setCorreo("esto-no-es-un-correo");

        assertThrows(CamposInvalidosException.class,
                () -> usuarioService.registrarComprador(requestValido));
    }

    @Test
    void registrarComprador_contrasenaFaltante_lanzaCamposInvalidosException() {
        requestValido.setContrasena("");

        assertThrows(CamposInvalidosException.class,
                () -> usuarioService.registrarComprador(requestValido));
    }

    @Test
    void registrarComprador_contrasenaCorta_lanzaCamposInvalidosException() {
        requestValido.setContrasena("123");

        assertThrows(CamposInvalidosException.class,
                () -> usuarioService.registrarComprador(requestValido));
    }

    // Helper para verificar que el usuario guardado tiene el hash y NO la contraseña real
    private Usuario argThatUsuarioConHash(String hashEsperado) {
        return org.mockito.ArgumentMatchers.argThat(u ->
                u.getContrasenaHash().equals(hashEsperado)
                        && !u.getContrasenaHash().equals("clave12345")
        );
    }

}
