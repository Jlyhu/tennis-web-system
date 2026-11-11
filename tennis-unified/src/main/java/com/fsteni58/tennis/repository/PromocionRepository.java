
package com.fsteni58.tennis.repository;

import com.fsteni58.tennis.dto.PromocionResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class PromocionRepository {

    private final JdbcTemplate jdbcTemplate;

    public PromocionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PromocionResponse> obtenerRecomendadas(UUID compradorId) {

        String sql = """
            WITH categorias_interes AS (

                SELECT LOWER(categoria) AS categoria
                FROM intereses_comprador
                WHERE comprador_id = ?
                  AND activo = TRUE

            ),

            categorias_compradas AS (

                SELECT DISTINCT LOWER(pr.categoria) AS categoria
                FROM pedidos pe
                JOIN detalle_pedido dp
                    ON dp.pedido_id = pe.id
                JOIN productos pr
                    ON pr.id = dp.producto_id
                WHERE pe.comprador_id = ?
                  AND pe.fecha_creacion >= NOW() - INTERVAL '90 days'
                  AND pe.estado <> 'CANCELADO'

            ),

            categorias_comprador AS (

                SELECT categoria FROM categorias_interes
                UNION
                SELECT categoria FROM categorias_compradas

            )

            SELECT DISTINCT
                promo.id,
                promo.nombre,
                promo.descripcion,
                promo.tipo_descuento,
                promo.valor,
                promo.fecha_inicio,
                promo.fecha_fin,
                pr.id AS producto_id,
                pr.nombre AS nombre_producto,
                pr.categoria,
                pr.precio AS precio_original,

                CASE
                    WHEN promo.tipo_descuento = 'PORCENTAJE'
                    THEN ROUND(
                        pr.precio * (1 - promo.valor / 100), 2
                    )
                    ELSE GREATEST(
                        pr.precio - promo.valor, 0
                    )
                END AS precio_final,

                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM intereses_comprador ic
                        WHERE ic.comprador_id = ?
                          AND ic.activo = TRUE
                          AND LOWER(ic.categoria) =
                              LOWER(pr.categoria)
                    )
                    THEN 'INTERES'
                    ELSE 'COMPRA_RECIENTE'
                END AS motivo

            FROM promociones promo

            JOIN promocion_producto pp
                ON pp.promocion_id = promo.id
               AND pp.activo = TRUE

            JOIN productos pr
                ON pr.id = pp.producto_id
               AND pr.activo = TRUE
               AND pr.stock > 0

            JOIN categorias_comprador cc
                ON cc.categoria = LOWER(pr.categoria)

            WHERE promo.activo = TRUE
              AND NOW() >= promo.fecha_inicio
              AND NOW() < promo.fecha_fin

            ORDER BY promo.fecha_fin ASC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->

            new PromocionResponse(
                rs.getObject("id", UUID.class),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("tipo_descuento"),
                rs.getBigDecimal("valor"),
                rs.getObject("fecha_inicio", OffsetDateTime.class),
                rs.getObject("fecha_fin", OffsetDateTime.class),
                rs.getObject("producto_id", UUID.class),
                rs.getString("nombre_producto"),
                rs.getString("categoria"),
                rs.getBigDecimal("precio_original"),
                rs.getBigDecimal("precio_final"),
                rs.getString("motivo")
            ),

            compradorId,
            compradorId,
            compradorId
        );
    }
}
