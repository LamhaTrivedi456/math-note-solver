package com.mathnotes.mathnotesolver.service;

import com.mathnotes.mathnotesolver.ast.ExpressionNode;
import com.mathnotes.mathnotesolver.dto.SolveResponse;
import com.mathnotes.mathnotesolver.equation.LinearEquationSolver;
import com.mathnotes.mathnotesolver.evaluator.StepEvaluator;
import com.mathnotes.mathnotesolver.parser.ExpressionParser;
import com.mathnotes.mathnotesolver.tokenizer.Token;
import com.mathnotes.mathnotesolver.tokenizer.TokenPreprocessor;
import com.mathnotes.mathnotesolver.tokenizer.TokenType;
import com.mathnotes.mathnotesolver.tokenizer.Tokenizer;
import com.mathnotes.mathnotesolver.util.NumberFormatter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.stereotype.Service;

@Service
public class MathSolverService {

    private static final int MAX_INPUT_LENGTH = 500;

    private final Tokenizer tokenizer = new Tokenizer();
    private final ExpressionParser parser = new ExpressionParser();
    private final StepEvaluator evaluator = new StepEvaluator();
    private final LinearEquationSolver equationSolver = new LinearEquationSolver();

    public SolveResponse solve(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Please enter an expression or an equation");
        }
        if (input.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Input is too long (maximum " + MAX_INPUT_LENGTH + " characters)");
        }

        List<Token> tokens = TokenPreprocessor.insertImplicitMultiplication(tokenizer.tokenize(input));
        long equalsCount = tokens.stream().filter(t -> t.type() == TokenType.EQUALS).count();

        if (equalsCount == 0) {
            return solveExpression(tokens);
        }
        if (equalsCount == 1) {
            return solveEquation(tokens);
        }
        throw new IllegalArgumentException("An equation can contain only one '=' sign");
    }

    private SolveResponse solveExpression(List<Token> tokens) {
        ExpressionNode ast = parser.parse(tokens);
        StepEvaluator.Result result = evaluator.evaluate(ast);
        return SolveResponse.ofSuccess("expression", result.steps(), NumberFormatter.format(result.value()));
    }

    private SolveResponse solveEquation(List<Token> tokens) {
        Set<Character> variables = new TreeSet<>();
        for (Token token : tokens) {
            if (token.type() == TokenType.VARIABLE) {
                variables.add(token.text().charAt(0));
            }
        }
        if (variables.isEmpty()) {
            throw new IllegalArgumentException("The equation has no variable to solve for");
        }
        if (variables.size() > 1) {
            throw new IllegalArgumentException("Only one variable is supported, found: " + variables);
        }
        char variable = variables.iterator().next();

        int equalsIndex = tokens.indexOf(new Token(TokenType.EQUALS, "="));
        ExpressionNode left = parser.parse(tokens.subList(0, equalsIndex));
        ExpressionNode right = parser.parse(tokens.subList(equalsIndex + 1, tokens.size()));

        LinearEquationSolver.Solution solution = equationSolver.solve(left, right, variable);
        return SolveResponse.ofSuccess("equation", solution.steps(), solution.answer());
    }
}