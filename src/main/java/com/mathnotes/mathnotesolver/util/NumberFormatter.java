package com.mathnotes.mathnotesolver.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberFormatter {

    private NumberFormatter() {
    }

    public static String format(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new ArithmeticException("Result is not a finite number");
        }
        if (Math.abs(value) < 1e15 && Math.abs(value - Math.rint(value)) < 1e-9) {
            return Long.toString(Math.round(value));
        }
        return BigDecimal.valueOf(value)
                .setScale(6, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }
}