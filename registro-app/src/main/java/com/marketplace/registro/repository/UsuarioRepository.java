package com.marketplace.registro.repository;

import com.marketplace.registro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Se usa para validar que el correo no esté repetido antes de insertar
    boolean existsByCorreoIgnoreCase(String correo);

}
