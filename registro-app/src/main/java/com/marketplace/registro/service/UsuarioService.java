package com.marketplace.registro.service;

import com.marketplace.registro.dto.RegistroRequest;
import com.marketplace.registro.dto.RegistroResponse;
import com.marketplace.registro.exception.CamposInvalidosException;
import com.marketplace.registro.exception.CorreoDuplicadoException;
import com.marketplace.registro.model.Rol;
import com.marketplace.registro.model.Usuario;
import com.marketplace.registro.repository.RolRepository;
import com.marketplace.registro.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private static final String ROL_COMPRADOR = "comprador";
    private static final Pattern CORREO_REGEX =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegistroResponse registrarComprador(RegistroRequest request) {
        validarCampos(request);

        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        // Validación de correo único (además del constraint unique en la BD)
        if (usuarioRepository.existsByCorreoIgnoreCase(correoNormalizado)) {
            throw new CorreoDuplicadoException("El correo ya está registrado: " + correoNormalizado);
        }

        Rol rolComprador = rolRepository.findByNombreRol(ROL_COMPRADOR)
                .orElseThrow(() -> new IllegalStateException(
                        "El rol 'comprador' no existe en la tabla roles"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre().trim());
        usuario.setCorreo(correoNormalizado);
        // Nunca se guarda la contraseña real, solo su hash con BCrypt (incluye salt)
        usuario.setContrasenaHash(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(rolComprador);
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);

        return new RegistroResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getCorreo(),
                ROL_COMPRADOR,
                "Registro exitoso"
        );
    }

    private void validarCampos(RegistroRequest request) {
        if (request == null) {
            throw new CamposInvalidosException("La solicitud no puede estar vacía");
        }
        if (esVacio(request.getNombre())) {
            throw new CamposInvalidosException("El nombre es obligatorio");
        }
        if (esVacio(request.getCorreo())) {
            throw new CamposInvalidosException("El correo es obligatorio");
        }
        if (esVacio(request.getContrasena())) {
            throw new CamposInvalidosException("La contraseña es obligatoria");
        }
        if (!CORREO_REGEX.matcher(request.getCorreo().trim()).matches()) {
            throw new CamposInvalidosException("El correo no tiene un formato válido");
        }
        if (request.getContrasena().length() < 8) {
            throw new CamposInvalidosException("La contraseña debe tener al menos 8 caracteres");
        }
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

}
