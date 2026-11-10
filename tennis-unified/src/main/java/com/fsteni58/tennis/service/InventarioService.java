package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.MovimientoRequest;
import com.fsteni58.tennis.exception.ProductoNoEncontradoException;
import com.fsteni58.tennis.repository.InventarioRepository;
import com.fsteni58.tennis.repository.InventarioRepository.Movimiento;
import com.fsteni58.tennis.repository.InventarioRepository.StockInventario;
import com.fsteni58.tennis.repository.ProductoRepository;
import com.fsteni58.tennis.repository.ProveedorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/** Lógica del inventario normalizado: movimientos, stock mínimo y consulta de stock. */
@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public InventarioService(InventarioRepository inventarioRepository,
                             ProductoRepository productoRepository,
                             ProveedorRepository proveedorRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<StockInventario> listarStock(UUID proveedorId) {
        if (!proveedorRepository.existeActivo(proveedorId)) {
            throw new IllegalArgumentException("El proveedor indicado no existe o no está activo");
        }
        return inventarioRepository.listarStock(proveedorId);
    }

    public List<Movimiento> listarMovimientos(UUID productoId) {
        verificarProducto(productoId);
        return inventarioRepository.listarMovimientos(productoId);
    }

    /**
     * Registra una entrada, salida o ajuste.
     * @throws IllegalArgumentException si la cantidad es inválida o el movimiento deja el stock en negativo
     */
    public void registrarMovimiento(MovimientoRequest r) {
        verificarProducto(r.productoId());
        int cantidad = r.cantidad();
        int conSigno;
        switch (r.tipo()) {
            case "entrada" -> {
                if (cantidad <= 0) throw new IllegalArgumentException("La cantidad de una entrada debe ser mayor a 0");
                conSigno = cantidad;
            }
            case "salida" -> {
                if (cantidad <= 0) throw new IllegalArgumentException("La cantidad de una salida debe ser mayor a 0");
                conSigno = -cantidad;
            }
            default -> {
                if (cantidad == 0) throw new IllegalArgumentException("El ajuste no puede ser 0");
                conSigno = cantidad;
            }
        }
        try {
            inventarioRepository.registrarMovimiento(r.productoId(), r.tipo(), conSigno, r.motivo());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Stock insuficiente: el movimiento deja el stock en negativo");
        }
    }

    public void actualizarStockMinimo(UUID productoId, int stockMinimo) {
        verificarProducto(productoId);
        inventarioRepository.actualizarStockMinimo(productoId, stockMinimo);
    }

    private void verificarProducto(UUID productoId) {
        if (!productoRepository.existeProducto(productoId)) {
            throw new ProductoNoEncontradoException("No existe un producto activo con id " + productoId);
        }
    }
}