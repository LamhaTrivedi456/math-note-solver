package com.mathnotes.mathnotesolver.ast;

public record BinaryOperationNode(char operator, ExpressionNode left, ExpressionNode right)
        implements ExpressionNode {

    @Override
    public double evaluate() {
        double a = left.evaluate();
        double b = right.evaluate();
        double result = switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield a / b;
            }
            case '^' -> Math.pow(a, b);
            default -> throw new IllegalStateException("Unknown operator: " + operator);
        };
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new ArithmeticException("Result is not a finite number");
        }
        return result;
    }
}