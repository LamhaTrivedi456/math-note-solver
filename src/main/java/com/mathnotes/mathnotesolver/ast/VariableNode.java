package com.mathnotes.mathnotesolver.ast;

public record VariableNode(char name) implements ExpressionNode {

    @Override
    public double evaluate() {
        throw new IllegalArgumentException(
                "'" + name + "' is a variable. Write an equation such as 2" + name + "+1=7 to solve for it");
    }
}