# Math Note Solver ✍️


**Live demo:** https://math-note-solver.onrender.com (free tier, may take about a minute to wake up)


A web application that solves arithmetic expressions and linear equations with step-by-step explanations.
Inspired by the iPad Math Notes feature. The parsing engine is written from scratch in Java, with no expression library.

## Features
- Expressions such as `5+3*2`, `(10/2)+3*4`, `3+4*2^2`, `2(3+4)`
- Linear equations such as `2x+3x-4=21`, `-x+10=4`, `3x+2=x+10`
- Every calculation step is shown
- Clear errors for invalid input, division by zero, non-linear equations

## How it works
```
Browser (HTML/CSS/JS) -> POST /api/solve -> MathController -> MathSolverService
  -> Tokenizer -> Shunting-yard parser -> AST -> StepEvaluator / LinearEquationSolver
```
- **Tokenizer**: one pass over the input, O(N).
- **Parser**: stack-based Shunting-yard. Each token is pushed and popped at most once, so parsing is O(N). AST nodes are built as operators are applied.
- **AST**: immutable Java records (`NumberNode`, `VariableNode`, `UnaryOperationNode`, `BinaryOperationNode`) in a sealed interface.
- **Evaluator**: post-order traversal of the tree that records each intermediate step.
- **Equation solver**: converts each side of the AST to a linear form `a*x + b`, then moves terms and divides.

## Tech stack
Java 21, Spring Boot, Maven, JUnit 5, HTML/CSS/JavaScript (Fetch API). No database.

## Run locally (Windows)
```
git clone <your-repo-url>
cd mathnotesolver
run.bat
```
Open http://localhost:8080

## Run the tests
```
mvnw.cmd test
```