package com.mathnotes.mathnotesolver.evaluator;

import static com.mathnotes.mathnotesolver.util.NumberFormatter.format;

import com.mathnotes.mathnotesolver.ast.BinaryOperationNode;
import com.mathnotes.mathnotesolver.ast.ExpressionNode;
import com.mathnotes.mathnotesolver.ast.NumberNode;
import com.mathnotes.mathnotesolver.ast.UnaryOperationNode;
import com.mathnotes.mathnotesolver.ast.VariableNode;
import java.util.ArrayList;
import java.util.List;

public class StepEvaluator {

    public record Result(double value, List<String> steps) {
        public Result {
            steps = List.copyOf(steps);
        }
    }

    public Result evaluate(ExpressionNode root) {
        List<String> steps = new ArrayList<>();
        double value = eval(root, steps);
        return new Result(value, steps);
    }

    private double eval(ExpressionNode node, List<String> steps) {
        return switch (node) {
            case NumberNode n -> n.value();
            case VariableNode v -> v.evaluate();
            case UnaryOperationNode u -> {
                double operand = eval(u.operand(), steps);
                double result = new UnaryOperationNode(u.operator(), new NumberNode(operand)).evaluate();
                if (!(u.operand() instanceof NumberNode)) {
                    steps.add("-(" + format(operand) + ") = " + format(result));
                }
                yield result;
            }
            case BinaryOperationNode b -> {
                double left = eval(b.left(), steps);
                double right = eval(b.right(), steps);
                double result = new BinaryOperationNode(
                        b.operator(), new NumberNode(left), new NumberNode(right)).evaluate();
                steps.add(show(left) + " " + b.operator() + " " + show(right) + " = " + format(result));
                yield result;
            }
        };
    }

    private String show(double value) {
        String text = format(value);
        return value < 0 ? "(" + text + ")" : text;
    }
}