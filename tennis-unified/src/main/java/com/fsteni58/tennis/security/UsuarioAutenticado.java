package com.fsteni58.tennis.security;

import java.io.Serializable;
import java.util.UUID;

/** Datos del usuario que inició sesión; viven en la sesión del servidor. */
public record UsuarioAutenticado(UUID id, String nombre, String rol, UUID proveedorId) implements Serializable {}