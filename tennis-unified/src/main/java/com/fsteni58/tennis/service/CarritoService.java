/*
 * Programa:     CarritoService.java
 * Versión:      1.0
 * Fecha:        06/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Lógica de negocio del carrito de compras (Comprador):
 *               ver el carrito, agregar productos, cambiar cantidades,
 *               quitar productos, vaciarlo y pagar. Solo los usuarios con
 *               rol comprador pueden usarlo, y la cantidad de cada
 *               producto no puede superar su stock. El pago es una
 *               simulación: no cobra nada, pero descuenta el stock y
 *               cierra el carrito.
 */
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.CarritoItemResponse;
import com.fsteni58.tennis.dto.CarritoResponse;
import com.fsteni58.tennis.exception.ProductoNoEncontradoException;
import com.fsteni58.tennis.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class CarritoService {

    /** Métodos de pago aceptados en la simulación. */
    private static final Set<String> METODOS_PAGO = Set.of("tarjeta", "pse", "contraentrega");

    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    /**
     * Consulta el carrito activo de un comprador. Si no tiene, devuelve un carrito vacío.
     * @param usuarioId id del comprador
     * @return el carrito con sus ítems y el total
     * @throws IllegalArgumentException si el usuario no es un comprador activo
     */
    public CarritoResponse verCarrito(UUID usuarioId) {
        validarComprador(usuarioId);
        return carritoRepository.buscarCarritoActivo(usuarioId)
                .map(this::respuesta)
                .orElse(new CarritoResponse(List.of()));
    }

    /**
     * Agrega un producto al carrito; si ya estaba, suma la cantidad. Crea el
     * carrito si el comprador no tenía uno activo.
     * @param usuarioId id del comprador
     * @param productoId id del producto
     * @param cantidad unidades a agregar, al menos 1
     * @return el carrito actualizado
     * @throws IllegalArgumentException si la cantidad es menor a 1, si el usuario
     *         no es comprador, o si la cantidad total supera el stock
     * @throws ProductoNoEncontradoException si el producto no existe o no está activo
     */
    @Transactional
    public CarritoResponse agregar(UUID usuarioId, UUID productoId, int cantidad) {
        validarComprador(usuarioId);
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1");
        }
        int stock = stockDe(productoId);
        UUID carritoId = carritoRepository.obtenerOCrearCarritoActivo(usuarioId);
        int yaEnCarrito = carritoRepository.cantidadEnCarrito(carritoId, productoId);
        if (yaEnCarrito + cantidad > stock) {
            throw new IllegalArgumentException("Solo hay " + stock + " unidades disponibles de este producto");
        }
        carritoRepository.agregarOSumar(carritoId, productoId, cantidad);
        return respuesta(carritoId);
    }

    /**
     * Cambia la cantidad de un producto que ya está en el carrito.
     * @param usuarioId id del comprador
     * @param productoId id del producto
     * @param cantidad nueva cantidad, entre 1 y el stock
     * @return el carrito actualizado
     * @throws IllegalArgumentException si la cantidad es menor a 1 o supera el stock,
     *         o si el producto no está en el carrito
     * @throws ProductoNoEncontradoException si el producto no existe o no está activo
     */
    @Transactional
    public CarritoResponse cambiarCantidad(UUID usuarioId, UUID productoId, int cantidad) {
        validarComprador(usuarioId);
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos 1; para quitar el producto usa la opción de eliminar");
        }
        UUID carritoId = carritoActivoOError(usuarioId);
        int stock = stockDe(productoId);
        if (cantidad > stock) {
            throw new IllegalArgumentException("Solo hay " + stock + " unidades disponibles de este producto");
        }
        if (carritoRepository.actualizarCantidad(carritoId, productoId, cantidad) == 0) {
            throw new IllegalArgumentException("Ese producto no está en tu carrito");
        }
        return respuesta(carritoId);
    }

    /**
     * Quita un producto del carrito.
     * @param usuarioId id del comprador
     * @param productoId id del producto
     * @return el carrito actualizado
     * @throws IllegalArgumentException si el producto no está en el carrito
     */
    @Transactional
    public CarritoResponse quitar(UUID usuarioId, UUID productoId) {
        validarComprador(usuarioId);
        UUID carritoId = carritoActivoOError(usuarioId);
        if (carritoRepository.quitarItem(carritoId, productoId) == 0) {
            throw new IllegalArgumentException("Ese producto no está en tu carrito");
        }
        return respuesta(carritoId);
    }

    /**
     * Quita todos los productos del carrito.
     * @param usuarioId id del comprador
     * @return el carrito vacío
     */
    @Transactional
    public CarritoResponse vaciar(UUID usuarioId) {
        validarComprador(usuarioId);
        carritoRepository.buscarCarritoActivo(usuarioId).ifPresent(carritoRepository::vaciar);
        return new CarritoResponse(List.of());
    }

    /**
     * Simula el pago del carrito. No cobra nada: valida el método de pago,
     * descuenta el stock de cada producto y marca el carrito como comprado.
     * Todo ocurre en una transacción: si algún producto ya no tiene stock
     * suficiente, no se descuenta nada y el carrito queda como estaba.
     * @param usuarioId id del comprador
     * @param metodoPago "tarjeta", "pse" o "contraentrega"
     * @return mensaje, referencia de la compra, método, total y cantidad de unidades
     * @throws IllegalArgumentException si el método no es válido, el carrito está
     *         vacío o falta stock de algún producto
     */
    @Transactional
    public Map<String, Object> pagar(UUID usuarioId, String metodoPago) {
        validarComprador(usuarioId);
        if (metodoPago == null || !METODOS_PAGO.contains(metodoPago)) {
            throw new IllegalArgumentException("Elige un método de pago válido");
        }
        UUID carritoId = carritoActivoOError(usuarioId);
        List<CarritoItemResponse> items = carritoRepository.listarItems(carritoId);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Tu carrito está vacío");
        }
        for (CarritoItemResponse item : items) {
            if (carritoRepository.descontarStock(item.getProductoId(), item.getCantidad()) == 0) {
                throw new IllegalArgumentException("No hay stock suficiente de " + item.getNombre());
            }
        }
        carritoRepository.marcarComprado(carritoId);

        CarritoResponse resumen = new CarritoResponse(items);
        String referencia = "FST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return Map.of(
                "mensaje", "Pago aprobado (simulación)",
                "referencia", referencia,
                "metodoPago", metodoPago,
                "total", resumen.getTotal(),
                "cantidadProductos", resumen.getCantidadTotal()
        );
    }

    /** Verifica que el usuario sea un comprador activo; si no, lanza error. */
    private void validarComprador(UUID usuarioId) {
        if (!carritoRepository.existeComprador(usuarioId)) {
            throw new IllegalArgumentException("Solo los compradores con sesión activa pueden usar el carrito");
        }
    }

    /** Devuelve el id del carrito activo del usuario, o lanza error si no tiene. */
    private UUID carritoActivoOError(UUID usuarioId) {
        return carritoRepository.buscarCarritoActivo(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Tu carrito está vacío"));
    }

    /** Devuelve el stock de un producto activo, o lanza ProductoNoEncontradoException. */
    private int stockDe(UUID productoId) {
        return carritoRepository.stockDeProductoActivo(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("No existe un producto activo con id " + productoId));
    }

    /** Arma la respuesta de un carrito a partir de su id. */
    private CarritoResponse respuesta(UUID carritoId) {
        return new CarritoResponse(carritoRepository.listarItems(carritoId));
    }
}