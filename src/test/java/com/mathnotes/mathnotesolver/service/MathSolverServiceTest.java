package com.mathnotes.mathnotesolver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mathnotes.mathnotesolver.dto.SolveResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class MathSolverServiceTest {

    private final MathSolverService service = new MathSolverService();

    @Test
    void solvesExpression() {
        SolveResponse response = service.solve("5+3*2");
        assertEquals("expression", response.type());
        assertEquals("11", response.answer());
        assertEquals(List.of("3 * 2 = 6", "5 + 6 = 11"), response.steps());
    }

    @Test
    void solvesExponent() {
        assertEquals("19", service.solve("3+4*2^2").answer());
    }

    @Test
    void solvesEquation() {
        SolveResponse response = service.solve("2x+3x-4=21");
        assertEquals("equation", response.type());
        assertEquals("x = 5", response.answer());
    }

    @Test
    void supportsImplicitMultiplication() {
        SolveResponse response = service.solve("2(3+4)");
        assertEquals("14", response.answer());
        assertEquals(List.of("3 + 4 = 7", "2 * 7 = 14"), response.steps());
    }

    @Test
    void avoidsFloatingPointNoise() {
        assertEquals("0.3", service.solve("0.1+0.2").answer());
    }

    @Test
    void rejectsBadInput() {
        assertThrows(IllegalArgumentException.class, () -> service.solve(""));
        assertThrows(IllegalArgumentException.class, () -> service.solve("5 +"));
        assertThrows(IllegalArgumentException.class, () -> service.solve("1=1=1"));
        assertThrows(IllegalArgumentException.class, () -> service.solve("2+3=5"));
        assertThrows(IllegalArgumentException.class, () -> service.solve("2x+y=5"));
    }

    @Test
    void rejectsDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> service.solve("10/0"));
    }
}