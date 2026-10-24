package com.mathnotes.mathnotesolver.equation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mathnotes.mathnotesolver.ast.ExpressionNode;
import com.mathnotes.mathnotesolver.parser.ExpressionParser;
import com.mathnotes.mathnotesolver.tokenizer.TokenPreprocessor;
import com.mathnotes.mathnotesolver.tokenizer.Tokenizer;
import java.util.List;
import org.junit.jupiter.api.Test;

class LinearEquationSolverTest {

    private final Tokenizer tokenizer = new Tokenizer();
    private final ExpressionParser parser = new ExpressionParser();
    private final LinearEquationSolver solver = new LinearEquationSolver();

    private ExpressionNode side(String text) {
        return parser.parse(TokenPreprocessor.insertImplicitMultiplication(tokenizer.tokenize(text)));
    }

    private LinearEquationSolver.Solution solve(String left, String right) {
        return solver.solve(side(left), side(right), 'x');
    }

    @Test
    void solvesBasicEquation() {
        LinearEquationSolver.Solution solution = solve("2x+3x-4", "21");
        assertEquals(List.of(
                "Combine like terms: 5x - 4 = 21",
                "Add 4 to both sides: 5x = 25",
                "Divide both sides by 5: x = 5"), solution.steps());
        assertEquals("x = 5", solution.answer());
    }

    @Test
    void solvesNegativeCoefficient() {
        assertEquals("x = 6", solve("-x+10", "4").answer());
    }

    @Test
    void solvesVariablesOnBothSides() {
        LinearEquationSolver.Solution solution = solve("3x+2", "x+10");
        assertEquals(List.of(
                "Combine like terms: 3x + 2 = x + 10",
                "Subtract x from both sides: 2x + 2 = 10",
                "Subtract 2 from both sides: 2x = 8",
                "Divide both sides by 2: x = 4"), solution.steps());
        assertEquals("x = 4", solution.answer());
    }

    @Test
    void detectsNoSolution() {
        assertEquals("No solution", solve("x+1", "x+2").answer());
    }

    @Test
    void detectsInfiniteSolutions() {
        assertEquals("Infinitely many solutions", solve("2x", "x+x").answer());
    }

    @Test
    void rejectsNonLinear() {
        assertThrows(IllegalArgumentException.class, () -> solve("x*x", "4"));
    }

    @Test
    void solvesDecimalCoefficient() {
        assertEquals("x = 4", solve("0.5x", "2").answer());
    }
}