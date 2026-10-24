package com.mathnotes.mathnotesolver.parser;

import com.mathnotes.mathnotesolver.ast.BinaryOperationNode;
import com.mathnotes.mathnotesolver.ast.ExpressionNode;
import com.mathnotes.mathnotesolver.ast.NumberNode;
import com.mathnotes.mathnotesolver.ast.UnaryOperationNode;
import com.mathnotes.mathnotesolver.ast.VariableNode;
import com.mathnotes.mathnotesolver.tokenizer.Token;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ExpressionParser {

    public ExpressionNode parse(List<Token> tokens) {
        Deque<ExpressionNode> nodes = new ArrayDeque<>();
        Deque<String> ops = new ArrayDeque<>();
        boolean expectOperand = true;

        for (Token token : tokens) {
            switch (token.type()) {
                case NUMBER -> {
                    if (!expectOperand) {
                        throw new IllegalArgumentException("Unexpected number '" + token.text() + "'");
                    }
                    double value;
                    try {
                        value = Double.parseDouble(token.text());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid number '" + token.text() + "'");
                    }
                    nodes.push(new NumberNode(value));
                    expectOperand = false;
                }
                case VARIABLE -> {
                    if (!expectOperand) {
                        throw new IllegalArgumentException("Unexpected variable '" + token.text() + "'");
                    }
                    nodes.push(new VariableNode(token.text().charAt(0)));
                    expectOperand = false;
                }
                case OPERATOR -> {
                    String op = token.text();
                    if (expectOperand) {
                        if (op.equals("-")) {
                            ops.push("neg");
                        } else if (!op.equals("+")) {
                            throw new IllegalArgumentException("Unexpected operator '" + op + "'");
                        }
                    } else {
                        while (!ops.isEmpty() && !ops.peek().equals("(")
                                && shouldPop(ops.peek(), op)) {
                            applyOperator(ops.pop(), nodes);
                        }
                        ops.push(op);
                        expectOperand = true;
                    }
                }
                case LEFT_PAREN -> {
                    if (!expectOperand) {
                        throw new IllegalArgumentException("Unexpected '('");
                    }
                    ops.push("(");
                }
                case RIGHT_PAREN -> {
                    if (expectOperand) {
                        throw new IllegalArgumentException("Unexpected ')'");
                    }
                    while (!ops.isEmpty() && !ops.peek().equals("(")) {
                        applyOperator(ops.pop(), nodes);
                    }
                    if (ops.isEmpty()) {
                        throw new IllegalArgumentException("Mismatched parentheses");
                    }
                    ops.pop();
                }
                default -> throw new IllegalArgumentException(
                        "'" + token.text() + "' is not allowed here");
            }
        }

        if (expectOperand) {
            throw new IllegalArgumentException("Expression is incomplete");
        }
        while (!ops.isEmpty()) {
            String op = ops.pop();
            if (op.equals("(")) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            applyOperator(op, nodes);
        }
        if (nodes.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return nodes.pop();
    }

    private boolean shouldPop(String top, String incoming) {
        int topPrec = precedence(top);
        int inPrec = precedence(incoming);
        return topPrec > inPrec || (topPrec == inPrec && !incoming.equals("^"));
    }

    private int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "neg" -> 3;
            case "^" -> 4;
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    private void applyOperator(String op, Deque<ExpressionNode> nodes) {
        if (op.equals("neg")) {
            if (nodes.isEmpty()) {
                throw new IllegalArgumentException("Invalid expression");
            }
            nodes.push(new UnaryOperationNode('-', nodes.pop()));
        } else {
            if (nodes.size() < 2) {
                throw new IllegalArgumentException("Invalid expression");
            }
            ExpressionNode right = nodes.pop();
            ExpressionNode left = nodes.pop();
            nodes.push(new BinaryOperationNode(op.charAt(0), left, right));
        }
    }
}