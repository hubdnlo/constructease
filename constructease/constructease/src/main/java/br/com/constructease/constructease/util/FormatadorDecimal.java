package br.com.constructease.constructease.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatadorDecimal {
    public static double arredondar(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}