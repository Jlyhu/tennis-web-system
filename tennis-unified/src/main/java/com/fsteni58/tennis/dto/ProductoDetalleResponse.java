package com.fsteni58.tennis.dto;

import java.math.BigDecimal;
import java.util.UUID;

/** Detalle de un producto del catálogo, incluido el proveedor que lo vende. */
public record ProductoDetalleResponse(UUID id, String nombre, String descripcion, String categoria,
                                      BigDecimal precio, int stock, String imagenUrl,
                                      UUID proveedorId, String proveedorNombre) {}