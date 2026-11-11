/*
 * Programa:     PrecioFormatter.java
 * Versión:      1.0
 * Fecha:        05/11/2026
 * Equipo:       Fsteni58 — Aponte Romero, Lizarazo Rincón, Alejo León,
 *               Moreno Cortes, Orosco Quemba
 * Descripción:  Utilidad para formatear valores monetarios en pesos
 *               colombianos (Comprador US3). Devuelve el texto formateado
 *               sin alterar el BigDecimal original, y protege contra
 *               precios nulos o negativos devolviendo null.
 */
package com.fsteni58.tennis.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class PrecioFormatter {

    private static final Locale LOCALE_CO = Locale.forLanguageTag("es-CO");
    private static final String PREFIJO_COP = "COP $ ";

    private PrecioFormatter() { /* utilidad estática */ }

    /**
     * Formatea un precio como pesos colombianos.
     * @param precio valor numérico original (puede ser null)
     * @return "COP $ 250.000,00" si el valor es válido; null si es nulo o negativo
     */
    public static String aCop(BigDecimal precio) {
        if (precio == null || precio.signum() < 0) {
            return null;
        }
        NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_CO);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return PREFIJO_COP + nf.format(precio);
    }
}