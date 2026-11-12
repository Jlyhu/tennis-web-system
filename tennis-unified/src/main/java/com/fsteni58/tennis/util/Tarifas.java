package com.fsteni58.tennis.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Parámetros acordados con el cliente: IVA 19 % y comisión de la plataforma 10 %. */
public final class Tarifas {

    public static final BigDecimal IVA_PORCENTAJE = new BigDecimal("19");
    public static final BigDecimal COMISION_PORCENTAJE = new BigDecimal("10");

    private Tarifas() { }

    /** IVA de un subtotal, redondeado a 2 decimales. */
    public static BigDecimal ivaDe(BigDecimal subtotal) {
        return subtotal.multiply(IVA_PORCENTAJE).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}