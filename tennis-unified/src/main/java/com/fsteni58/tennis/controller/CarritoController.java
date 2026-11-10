/*
 * Programa:     CarritoController.java
 * Versión:      1.0
 * Fecha:        06/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Controlador REST del carrito de compras (Comprador).
 *               Expone, bajo /carrito, las operaciones de ver el carrito,
 *               agregar, cambiar cantidad, quitar, vaciar y pagar (pago
 *               simulado). Recibe las peticiones HTTP y delega toda la
 *               lógica de negocio a CarritoService. Igual que los
 *               tickets, identifica al comprador con el parámetro
 *               usuarioId.
 */
package com.fsteni58.tennis.controller;

import com.fsteni58.tennis.dto.CarritoResponse;
import com.fsteni58.tennis.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    /**
     * Consulta el carrito activo de un comprador.
     * @param usuarioId id del comprador, como parámetro de consulta
     * @return 200 OK con el carrito (vacío si no tiene productos)
     */
    @GetMapping
    public ResponseEntity<CarritoResponse> ver(@RequestParam UUID usuarioId) {
        return ResponseEntity.ok(carritoService.verCarrito(usuarioId));
    }

    /**
     * Agrega un producto al carrito (o suma la cantidad si ya estaba).
     * @param usuarioId id del comprador
     * @param productoId id del producto
     * @param cantidad unidades a agregar
     * @return 200 OK con el carrito actualizado
     */
    @PostMapping("/items")
    public ResponseEntity<CarritoResponse> agregar(@RequestParam UUID usuarioId,
                                                   @RequestParam UUID productoId,
                                                   @RequestParam int cantidad) {
        return ResponseEntity.ok(carritoService.agregar(usuarioId, productoId, cantidad));
    }

    /**
     * Cambia la cantidad de un producto del carrito.
     * @param productoId id del producto, tomado de la URL
     * @param usuarioId id del comprador
     * @param cantidad nueva cantidad
     * @return 200 OK con el carrito actualizado
     */
    @PatchMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponse> cambiarCantidad(@PathVariable UUID productoId,
                                                           @RequestParam UUID usuarioId,
                                                           @RequestParam int cantidad) {
        return ResponseEntity.ok(carritoService.cambiarCantidad(usuarioId, productoId, cantidad));
    }

    /**
     * Quita un producto del carrito.
     * @param productoId id del producto, tomado de la URL
     * @param usuarioId id del comprador
     * @return 200 OK con el carrito actualizado
     */
    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponse> quitar(@PathVariable UUID productoId,
                                                  @RequestParam UUID usuarioId) {
        return ResponseEntity.ok(carritoService.quitar(usuarioId, productoId));
    }

    /**
     * Quita todos los productos del carrito.
     * @param usuarioId id del comprador
     * @return 200 OK con el carrito vacío
     */
    @DeleteMapping
    public ResponseEntity<CarritoResponse> vaciar(@RequestParam UUID usuarioId) {
        return ResponseEntity.ok(carritoService.vaciar(usuarioId));
    }

    /**
     * Simula el pago del carrito: descuenta el stock y cierra el carrito.
     * @param usuarioId id del comprador
     * @param metodoPago "nequi", "tarjeta" o "paypal"
     * @return 200 OK con el mensaje, la referencia, el método, el total y las unidades
     */
    @PostMapping("/pagar")
    public ResponseEntity<Map<String, Object>> pagar(@RequestParam UUID usuarioId,
                                                     @RequestParam String metodoPago) {
        return ResponseEntity.ok(carritoService.pagar(usuarioId, metodoPago));
    }
}