package com.mathnotes.mathnotesolver.ast;

public record NumberNode(double value) implements ExpressionNode {

    @Override
    public double evaluate() {
        return value;
    }
}