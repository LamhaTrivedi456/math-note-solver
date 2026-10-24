package com.mathnotes.mathnotesolver.tokenizer;

import java.util.ArrayList;
import java.util.List;

public final class TokenPreprocessor {

    private TokenPreprocessor() {
    }

    public static List<Token> insertImplicitMultiplication(List<Token> tokens) {
        List<Token> result = new ArrayList<>(tokens.size() * 2);
        for (int i = 0; i < tokens.size(); i++) {
            Token current = tokens.get(i);
            if (i > 0 && endsOperand(tokens.get(i - 1)) && startsOperand(current)) {
                result.add(new Token(TokenType.OPERATOR, "*"));
            }
            result.add(current);
        }
        return result;
    }

    private static boolean endsOperand(Token token) {
        return token.type() == TokenType.NUMBER
                || token.type() == TokenType.VARIABLE
                || token.type() == TokenType.RIGHT_PAREN;
    }

    private static boolean startsOperand(Token token) {
        return token.type() == TokenType.VARIABLE
                || token.type() == TokenType.LEFT_PAREN;
    }
}