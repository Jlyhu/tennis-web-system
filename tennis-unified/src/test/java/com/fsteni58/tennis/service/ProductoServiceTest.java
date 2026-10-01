/*
 * Programa:     ProductoServiceTest.java
 * Versión:      1.0
 * Fecha:        29/09/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Pruebas unitarias (caja blanca) de ProductoService.actualizarStock
 *               (Proveedor US5). Los repositorios se simulan con Mockito, por lo
 *               que no se requiere base de datos.
 */
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.exception.ProductoNoEncontradoException;
import com.fsteni58.tennis.repository.ProductoRepository;
import com.fsteni58.tennis.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    private ProductoService productoService;
    private final UUID productoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        productoService = new ProductoService(productoRepository, proveedorRepository);
    }

    // Caso Válido: stock = 40 sobre un producto existente
    @Test
    void actualizarStock_valorValido_actualizaEnRepositorio() {
        when(productoRepository.existeProducto(productoId)).thenReturn(true);

        assertDoesNotThrow(() -> productoService.actualizarStock(productoId, 40));

        verify(productoRepository).actualizarStock(productoId, 40);
    }

    // Caso Inválido: stock = -5
    @Test
    void actualizarStock_valorNegativo_lanzaIllegalArgumentExceptionYNoActualiza() {
        assertThrows(IllegalArgumentException.class,
                () -> productoService.actualizarStock(productoId, -5));

        verify(productoRepository, never()).actualizarStock(any(), anyInt());
    }

    // Caso Vacío: id ausente (null); el repositorio simulado responde que no existe
    @Test
    void actualizarStock_idNulo_lanzaProductoNoEncontradoYNoActualiza() {
        assertThrows(ProductoNoEncontradoException.class,
                () -> productoService.actualizarStock(null, 40));

        verify(productoRepository, never()).actualizarStock(any(), anyInt());
    }

    // Caso Límite: stock = 0, el mínimo permitido
    @Test
    void actualizarStock_stockCero_actualizaEnRepositorio() {
        when(productoRepository.existeProducto(productoId)).thenReturn(true);

        assertDoesNotThrow(() -> productoService.actualizarStock(productoId, 0));

        verify(productoRepository).actualizarStock(productoId, 0);
    }
}