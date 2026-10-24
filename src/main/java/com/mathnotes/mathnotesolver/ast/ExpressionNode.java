package com.mathnotes.mathnotesolver.ast;

public sealed interface ExpressionNode
        permits NumberNode, VariableNode, UnaryOperationNode, BinaryOperationNode {

    double evaluate();
}