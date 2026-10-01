/*
 * Programa:     ProveedorRepository.java
 * Versión:      1.0
 * Fecha:        01/10/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Repositorio de acceso a datos para el registro de
 *               proveedores (Comprador/Proveedor US1, registro). Ejecuta
 *               las consultas SQL sobre la tabla proveedores vía
 *               JdbcTemplate. Los campos nombreEmpresa, nit, telefono y
 *               direccion se incorporaron al registrar que AuthService
 *               los guardaba con un valor de relleno en vez de los datos
 *               reales del formulario.
 */
package com.fsteni58.tennis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ProveedorRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProveedorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserta un proveedor nuevo, activo por defecto, asociado a un usuario
     * ya registrado en la tabla usuarios.
     * @param usuarioId id del usuario (con rol proveedor) al que pertenece esta empresa
     * @param nombreEmpresa nombre comercial de la empresa
     * @param nit NIT de la empresa
     * @param telefono teléfono de contacto (puede ser nulo)
     * @param direccion dirección de la empresa (puede ser nula)
     * @return id generado para el nuevo proveedor
     */
    public UUID guardar(UUID usuarioId, String nombreEmpresa, String nit, String telefono, String direccion) {
        String sql = """
            INSERT INTO proveedores (usuario_id, nombre_empresa, nit, telefono, direccion, activo)
            VALUES (?, ?, ?, ?, ?, true)
            RETURNING id
            """;
        return jdbcTemplate.queryForObject(sql, UUID.class, usuarioId, nombreEmpresa, nit, telefono, direccion);
    }

    /**
     * Verifica si existe un proveedor activo con el id dado.
     * @param proveedorId id del proveedor
     * @return true si existe y está activo, false en caso contrario
     */
    public boolean existeActivo(UUID proveedorId) {
        String sql = "SELECT COUNT(*) FROM proveedores WHERE id = ? AND activo = true";
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, proveedorId);
        return total != null && total > 0;
    }
}