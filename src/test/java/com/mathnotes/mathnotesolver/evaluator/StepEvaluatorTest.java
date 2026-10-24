package com.mathnotes.mathnotesolver.evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mathnotes.mathnotesolver.parser.ExpressionParser;
import com.mathnotes.mathnotesolver.tokenizer.Tokenizer;
import java.util.List;
import org.junit.jupiter.api.Test;

class StepEvaluatorTest {

    private final Tokenizer tokenizer = new Tokenizer();
    private final ExpressionParser parser = new ExpressionParser();
    private final StepEvaluator evaluator = new StepEvaluator();

    private StepEvaluator.Result run(String input) {
        return evaluator.evaluate(parser.parse(tokenizer.tokenize(input)));
    }

    @Test
    void showsPrecedenceSteps() {
        StepEvaluator.Result result = run("5+3*2");
        assertEquals(11, result.value(), 1e-9);
        assertEquals(List.of("3 * 2 = 6", "5 + 6 = 11"), result.steps());
    }

    @Test
    void showsExponentSteps() {
        StepEvaluator.Result result = run("3+4*2^2");
        assertEquals(19, result.value(), 1e-9);
        assertEquals(List.of("2 ^ 2 = 4", "4 * 4 = 16", "3 + 16 = 19"), result.steps());
    }

    @Test
    void showsParenthesesSteps() {
        StepEvaluator.Result result = run("(10/2)+3*4");
        assertEquals(17, result.value(), 1e-9);
        assertEquals(List.of("10 / 2 = 5", "3 * 4 = 12", "5 + 12 = 17"), result.steps());
    }

    @Test
    void wrapsNegativeOperands() {
        StepEvaluator.Result result = run("2*(-3)");
        assertEquals(-6, result.value(), 1e-9);
        assertEquals(List.of("2 * (-3) = -6"), result.steps());
    }

    @Test
    void formatsDecimals() {
        StepEvaluator.Result result = run("7/2");
        assertEquals(3.5, result.value(), 1e-9);
        assertEquals(List.of("7 / 2 = 3.5"), result.steps());
    }

    @Test
    void rejectsVariables() {
        assertThrows(IllegalArgumentException.class, () -> run("x+1"));
    }
}