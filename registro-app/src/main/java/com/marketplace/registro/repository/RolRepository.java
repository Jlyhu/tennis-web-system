package com.marketplace.registro.repository;

import com.marketplace.registro.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RolRepository extends JpaRepository<Rol, UUID> {

    Optional<Rol> findByNombreRol(String nombreRol);

}
