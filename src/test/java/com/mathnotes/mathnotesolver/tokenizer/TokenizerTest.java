package com.mathnotes.mathnotesolver.tokenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class TokenizerTest {

    private final Tokenizer tokenizer = new Tokenizer();

    @Test
    void tokenizesSimpleExpression() {
        List<Token> tokens = tokenizer.tokenize("5 + 3 * 2");
        assertEquals(5, tokens.size());
        assertEquals(new Token(TokenType.NUMBER, "5"), tokens.get(0));
        assertEquals(new Token(TokenType.OPERATOR, "+"), tokens.get(1));
        assertEquals(new Token(TokenType.NUMBER, "2"), tokens.get(4));
    }

    @Test
    void tokenizesEquation() {
        List<Token> tokens = tokenizer.tokenize("2x+3=11");
        assertEquals(6, tokens.size());
        assertEquals(TokenType.VARIABLE, tokens.get(1).type());
        assertEquals(TokenType.EQUALS, tokens.get(4).type());
    }

    @Test
    void rejectsUnknownCharacter() {
        assertThrows(IllegalArgumentException.class, () -> tokenizer.tokenize("5 $ 3"));
    }
}