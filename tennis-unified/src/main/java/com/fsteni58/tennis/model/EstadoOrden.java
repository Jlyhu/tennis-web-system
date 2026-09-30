/*

Programa:     EstadoOrden.java
Versión:      1.0
Fecha:        30/09/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Enumeración (Enum) que define los diferentes estados por 
              los que puede transitar una orden de compra o pedido 
              dentro del sistema, desde su creación hasta su entrega 
              final o cancelación.
*/

package com.fsteni58.tennis.model;

/**
 * Representa el ciclo de vida y el estado actual de una orden en la tienda.
 * Facilita el control de flujo en la lógica de negocio y el seguimiento 
 * de los pedidos por parte de compradores y proveedores.
 */
public enum EstadoOrden {
    
    /**
     * La orden ha sido creada pero aún no ha comenzado a procesarse
     * (por ejemplo, a la espera de confirmación de pago o validación de inventario).
     */
    PENDIENTE,
    
    /**
     * La orden está siendo preparada, empaquetada o validada por el proveedor.
     */
    EN_PROCESO,
    
    /**
     * La orden ya fue despachada y se encuentra en manos de la transportadora 
     * o en camino hacia la dirección del cliente.
     */
    ENVIADO,
    
    /**
     * La orden ha llegado a su destino y fue recibida satisfactoriamente 
     * por el comprador. Este es un estado final.
     */
    ENTREGADO,
    
    /**
     * La orden fue anulada antes de completarse, ya sea por solicitud del 
     * cliente, rechazo de pago o falta de stock. Este es un estado final.
     */
    CANCELADO
}