package com.mathnotes.mathnotesolver.tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        int n = input.length();

        while (i < n) {
            char c = input.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
            } else if (Character.isDigit(c) || c == '.') {
                int start = i;
                boolean seenDot = false;
                while (i < n && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    if (input.charAt(i) == '.') {
                        if (seenDot) {
                            throw new IllegalArgumentException("Invalid number at position " + start);
                        }
                        seenDot = true;
                    }
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, input.substring(start, i)));
            } else if (Character.isLetter(c)) {
                tokens.add(new Token(TokenType.VARIABLE, String.valueOf(c)));
                i++;
            } else if ("+-*/^".indexOf(c) >= 0) {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                i++;
            } else if (c == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "("));
                i++;
            } else if (c == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")"));
                i++;
            } else if (c == '=') {
                tokens.add(new Token(TokenType.EQUALS, "="));
                i++;
            } else {
                throw new IllegalArgumentException("Unexpected character '" + c + "' at position " + i);
            }
        }
        return tokens;
    }
}