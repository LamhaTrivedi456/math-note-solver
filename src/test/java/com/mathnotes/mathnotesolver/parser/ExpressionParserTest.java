package com.mathnotes.mathnotesolver.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mathnotes.mathnotesolver.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

class ExpressionParserTest {

    private final Tokenizer tokenizer = new Tokenizer();
    private final ExpressionParser parser = new ExpressionParser();

    private double eval(String input) {
        return parser.parse(tokenizer.tokenize(input)).evaluate();
    }

    @Test
    void respectsPrecedence() {
        assertEquals(11, eval("5+3*2"), 1e-9);
        assertEquals(17, eval("(10/2)+3*4"), 1e-9);
        assertEquals(19, eval("3+4*2^2"), 1e-9);
    }

    @Test
    void handlesAssociativity() {
        assertEquals(3, eval("8-3-2"), 1e-9);
        assertEquals(512, eval("2^3^2"), 1e-9);
    }

    @Test
    void handlesUnaryMinus() {
        assertEquals(-4, eval("-2^2"), 1e-9);
        assertEquals(-6, eval("2*(-3)"), 1e-9);
    }

    @Test
    void rejectsDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> eval("10/0"));
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> eval("(3+4"));
        assertThrows(IllegalArgumentException.class, () -> eval("3 4"));
        assertThrows(IllegalArgumentException.class, () -> eval("3+"));
        assertThrows(IllegalArgumentException.class, () -> eval(""));
    }
}