package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.LiquidacionRequest;
import com.fsteni58.tennis.repository.LiquidacionRepository;
import com.fsteni58.tennis.repository.LiquidacionRepository.*;
import com.fsteni58.tennis.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/** Lógica de la liquidación a proveedores. La liquidación final desactiva al proveedor y sus productos (trigger en la base). */
@Service
public class LiquidacionService {

    private final LiquidacionRepository liquidacionRepository;
    private final ProveedorRepository proveedorRepository;

    public LiquidacionService(LiquidacionRepository liquidacionRepository, ProveedorRepository proveedorRepository) {
        this.liquidacionRepository = liquidacionRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public UUID crear(LiquidacionRequest r) {
        if (r.fechaFin().isBefore(r.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
        if (!proveedorRepository.existeActivo(r.proveedorId())) {
            throw new IllegalArgumentException("El proveedor indicado no existe o no está activo");
        }
        boolean esFinal = "final".equals(r.tipo());
        if (esFinal && r.motivoSalidaId() == null) {
            throw new IllegalArgumentException("La liquidación final requiere un motivo de salida");
        }
        if (!esFinal && r.motivoSalidaId() != null) {
            throw new IllegalArgumentException("La liquidación periódica no lleva motivo de salida");
        }
        return liquidacionRepository.crear(r.proveedorId(), r.fechaInicio(), r.fechaFin(), r.tipo(), r.motivoSalidaId());
    }

    public List<Liquidacion> listar() { return liquidacionRepository.listar(); }

    public List<Linea> detalle(UUID id) { return liquidacionRepository.detalle(id); }

    public void marcarPagada(UUID id) {
        if (liquidacionRepository.marcarPagada(id) == 0) {
            throw new IllegalArgumentException("La liquidación no existe o ya estaba pagada");
        }
    }

    public List<ProveedorOpcion> proveedores() { return liquidacionRepository.proveedoresActivos(); }

    public List<MotivoSalida> motivos() { return liquidacionRepository.motivosSalida(); }
}