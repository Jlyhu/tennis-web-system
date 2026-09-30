/*

Programa:     PedidoRepository.java
Versión:      1.0
Fecha:        30/09/2026
Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
              Moreno Cortes, Orosco Quemba  

Descripción:  Repositorio encargado de la persistencia y gestión de datos 
              para las órdenes de pedido. Actualmente implementa un 
              almacenamiento temporal en memoria utilizando una lista, 
              simulando las operaciones CRUD básicas sobre los pedidos 
              realizados, a la espera de la integración completa con base 
              de datos.
*/

package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.OrdenPedidoResponse;
import com.fsteni58.tennis.model.EstadoOrden;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Componente de acceso a datos (DAO) para la gestión de Pedidos.
 * Maneja el almacenamiento, consulta y actualización del estado de las órdenes.
 * Nota: Por el momento, utiliza una estructura en memoria (List) para simular 
 * la base de datos, aunque ya tiene inyectado JdbcTemplate para futuras migraciones.
 */
@Repository
public class PedidoRepository {

    private final JdbcTemplate jdbcTemplate;
    
    /** 
     * Lista en memoria que simula la tabla de órdenes en la base de datos.
     * Almacena objetos de tipo OrdenPedidoResponse.
     */
    private final List<OrdenPedidoResponse> ordenesBD = new ArrayList<>();

    /**
     * Constructor que inyecta la dependencia de JdbcTemplate.
     * 
     * @param jdbcTemplate herramienta de Spring para ejecutar consultas SQL (preparada para uso futuro)
     */
    public PedidoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Guarda una nueva orden de pedido en el almacenamiento en memoria.
     * 
     * @param orden el objeto DTO que representa la orden a guardar
     * @return la misma orden de pedido que fue registrada
     */
    public OrdenPedidoResponse guardar(OrdenPedidoResponse orden) {
        ordenesBD.add(orden);
        return orden;
    }

    /**
     * Busca una orden específica en el almacenamiento utilizando su identificador único.
     * 
     * @param id identificador UUID de la orden de pedido
     * @return un Optional que contiene la orden si fue encontrada, o Optional.empty() si no existe
     */
    public Optional<OrdenPedidoResponse> buscarPorId(UUID id) {
        return ordenesBD.stream()
                .filter(o -> o.id().equals(id))
                .findFirst();
    }

    /**
     * Obtiene una copia de la lista con todas las órdenes de pedido registradas
     * actualmente en el sistema.
     * 
     * @return Lista de todas las órdenes de pedido
     */
    public List<OrdenPedidoResponse> obtenerTodas() {
        return new ArrayList<>(ordenesBD);
    }

    /**
     * Obtiene una lista de órdenes filtrada según el estado en el que se encuentren
     * (por ejemplo: PENDIENTE, EN_PROCESO, ENTREGADO).
     * 
     * @param estado estado por el cual se desean filtrar las órdenes
     * @return Lista de órdenes que coinciden con el estado solicitado
     */
    public List<OrdenPedidoResponse> obtenerPorEstado(EstadoOrden estado) {
        return ordenesBD.stream()
                .filter(o -> o.estado() == estado)
                .toList();
    }

    /**
     * Actualiza el estado de una orden de pedido existente.
     * Al tratarse de un DTO/Record inmutable, busca la orden actual, crea una copia 
     * idéntica con el nuevo estado aplicado y reemplaza la anterior en la lista.
     * 
     * @param id          identificador UUID de la orden que se desea modificar
     * @param nuevoEstado el nuevo estado que se le asignará a la orden
     * @return la orden de pedido actualizada, o null si la orden no fue encontrada
     */
    public OrdenPedidoResponse actualizarEstado(UUID id, EstadoOrden nuevoEstado) {
        for (int i = 0; i < ordenesBD.size(); i++) {
            if (ordenesBD.get(i).id().equals(id)) {
                OrdenPedidoResponse anterior = ordenesBD.get(i);
                OrdenPedidoResponse actualizada = new OrdenPedidoResponse(
                        anterior.id(),
                        anterior.cliente(),
                        anterior.fecha(),
                        nuevoEstado,
                        anterior.items(),
                        anterior.total()
                );
                ordenesBD.set(i, actualizada);
                return actualizada;
            }
        }
        return null;
    }
}