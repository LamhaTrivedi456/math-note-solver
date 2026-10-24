package com.mathnotes.mathnotesolver.dto;

import java.util.List;

public record SolveResponse(boolean success, String type, List<String> steps, String answer, String error) {

    public static SolveResponse ofSuccess(String type, List<String> steps, String answer) {
        return new SolveResponse(true, type, steps, answer, null);
    }

    public static SolveResponse ofError(String error) {
        return new SolveResponse(false, null, List.of(), null, error);
    }
}