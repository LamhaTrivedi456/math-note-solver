package com.mathnotes.mathnotesolver.equation;

import static com.mathnotes.mathnotesolver.util.NumberFormatter.format;

import com.mathnotes.mathnotesolver.ast.BinaryOperationNode;
import com.mathnotes.mathnotesolver.ast.ExpressionNode;
import com.mathnotes.mathnotesolver.ast.NumberNode;
import com.mathnotes.mathnotesolver.ast.UnaryOperationNode;
import com.mathnotes.mathnotesolver.ast.VariableNode;
import java.util.ArrayList;
import java.util.List;

public class LinearEquationSolver {

    private static final double EPS = 1e-9;

    public record Solution(List<String> steps, String answer) {
        public Solution {
            steps = List.copyOf(steps);
        }
    }

    public Solution solve(ExpressionNode leftSide, ExpressionNode rightSide, char variable) {
        LinearForm left = toLinear(leftSide, variable);
        LinearForm right = toLinear(rightSide, variable);
        List<String> steps = new ArrayList<>();
        steps.add("Combine like terms: " + show(left, right, variable));

        if (!right.isConstant()) {
            String action = right.a() > 0
                    ? "Subtract " + LinearForm.term(right.a(), variable) + " from both sides"
                    : "Add " + LinearForm.term(-right.a(), variable) + " to both sides";
            left = new LinearForm(left.a() - right.a(), left.b());
            right = LinearForm.constant(right.b());
            steps.add(action + ": " + show(left, right, variable));
        }

        if (left.isConstant()) {
            return conclude(steps, left.b(), right.b());
        }

        if (Math.abs(left.b()) >= EPS) {
            String action = left.b() > 0
                    ? "Subtract " + format(left.b()) + " from both sides"
                    : "Add " + format(-left.b()) + " to both sides";
            right = LinearForm.constant(right.b() - left.b());
            left = new LinearForm(left.a(), 0);
            steps.add(action + ": " + show(left, right, variable));
        }

        if (Math.abs(left.a() - 1) >= EPS) {
            String action = "Divide both sides by " + format(left.a());
            double value = right.b() / left.a();
            left = LinearForm.variable();
            right = LinearForm.constant(value);
            steps.add(action + ": " + show(left, right, variable));
        }

        return new Solution(steps, variable + " = " + format(right.b()));
    }

    private Solution conclude(List<String> steps, double leftValue, double rightValue) {
        if (Math.abs(leftValue - rightValue) < EPS) {
            steps.add("Both sides are always equal");
            return new Solution(steps, "Infinitely many solutions");
        }
        steps.add("The two sides can never be equal");
        return new Solution(steps, "No solution");
    }

    private String show(LinearForm left, LinearForm right, char variable) {
        return left.display(variable) + " = " + right.display(variable);
    }

    private LinearForm toLinear(ExpressionNode node, char variable) {
        return switch (node) {
            case NumberNode n -> LinearForm.constant(n.value());
            case VariableNode v -> {
                if (v.name() != variable) {
                    throw new IllegalArgumentException("Only one variable is supported");
                }
                yield LinearForm.variable();
            }
            case UnaryOperationNode u -> toLinear(u.operand(), variable).negate();
            case BinaryOperationNode b -> {
                LinearForm l = toLinear(b.left(), variable);
                LinearForm r = toLinear(b.right(), variable);
                yield switch (b.operator()) {
                    case '+' -> l.plus(r);
                    case '-' -> l.minus(r);
                    case '*' -> l.times(r);
                    case '/' -> l.dividedBy(r);
                    case '^' -> l.power(r);
                    default -> throw new IllegalStateException("Unknown operator: " + b.operator());
                };
            }
        };
    }
}