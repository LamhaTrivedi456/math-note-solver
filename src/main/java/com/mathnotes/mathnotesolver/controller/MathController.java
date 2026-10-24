package com.mathnotes.mathnotesolver.controller;

import com.mathnotes.mathnotesolver.dto.SolveRequest;
import com.mathnotes.mathnotesolver.dto.SolveResponse;
import com.mathnotes.mathnotesolver.service.MathSolverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MathController {

    private final MathSolverService service;

    public MathController(MathSolverService service) {
        this.service = service;
    }

    @PostMapping("/solve")
    public ResponseEntity<SolveResponse> solve(@RequestBody SolveRequest request) {
        try {
            return ResponseEntity.ok(service.solve(request.input()));
        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.badRequest().body(SolveResponse.ofError(e.getMessage()));
        }
    }
}