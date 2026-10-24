package com.mathnotes.mathnotesolver.equation;

import static com.mathnotes.mathnotesolver.util.NumberFormatter.format;

public record LinearForm(double a, double b) {

    private static final double EPS = 1e-9;

    public static LinearForm constant(double value) {
        return new LinearForm(0, value);
    }

    public static LinearForm variable() {
        return new LinearForm(1, 0);
    }

    public boolean isConstant() {
        return Math.abs(a) < EPS;
    }

    public LinearForm negate() {
        return new LinearForm(-a, -b);
    }

    public LinearForm plus(LinearForm other) {
        return new LinearForm(a + other.a, b + other.b);
    }

    public LinearForm minus(LinearForm other) {
        return new LinearForm(a - other.a, b - other.b);
    }

    public LinearForm times(LinearForm other) {
        if (isConstant()) {
            return new LinearForm(b * other.a, b * other.b);
        }
        if (other.isConstant()) {
            return new LinearForm(a * other.b, b * other.b);
        }
        throw new IllegalArgumentException("Not a linear equation: a variable is multiplied by a variable");
    }

    public LinearForm dividedBy(LinearForm other) {
        if (!other.isConstant()) {
            throw new IllegalArgumentException("Not a linear equation: cannot divide by a variable");
        }
        if (Math.abs(other.b) < EPS) {
            throw new ArithmeticException("Division by zero");
        }
        return new LinearForm(a / other.b, b / other.b);
    }

    public LinearForm power(LinearForm exponent) {
        if (!exponent.isConstant()) {
            throw new IllegalArgumentException("Not a linear equation: the exponent contains a variable");
        }
        if (isConstant()) {
            return constant(Math.pow(b, exponent.b));
        }
        if (Math.abs(exponent.b - 1) < EPS) {
            return this;
        }
        if (Math.abs(exponent.b) < EPS) {
            return constant(1);
        }
        throw new IllegalArgumentException("Not a linear equation: the variable is raised to a power");
    }

    public static String term(double coefficient, char variable) {
        if (Math.abs(coefficient - 1) < EPS) {
            return String.valueOf(variable);
        }
        if (Math.abs(coefficient + 1) < EPS) {
            return "-" + variable;
        }
        return format(coefficient) + variable;
    }

    public String display(char variable) {
        if (isConstant()) {
            return format(b);
        }
        String term = term(a, variable);
        if (Math.abs(b) < EPS) {
            return term;
        }
        return b > 0 ? term + " + " + format(b) : term + " - " + format(-b);
    }
}