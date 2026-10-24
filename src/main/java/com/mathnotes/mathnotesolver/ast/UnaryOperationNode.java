package com.mathnotes.mathnotesolver.ast;

public record UnaryOperationNode(char operator, ExpressionNode operand) implements ExpressionNode {

    @Override
    public double evaluate() {
        return -operand.evaluate();
    }
}