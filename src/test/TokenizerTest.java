package test;

import main.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void testWithCommentLine() throws IOException {
        File inputFile = new File("TokenizerTests/test1.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("these", "are", "valid", "tokens");
        for (int i = 0; i < expectedTokens.size(); i++) {
            Assertions.assertTrue(tokenizer.hasNextToken());
            Assertions.assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        Assertions.assertFalse(tokenizer.hasNextToken());
    }

    @Test
    void testWithInlineComment() throws IOException {
        File inputFile = new File("TokenizerTests/test2.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("half", "(", "of", ")", "this", "line", "are", "comments");
        for (int i = 0; i < expectedTokens.size(); i++) {
            Assertions.assertTrue(tokenizer.hasNextToken());
            Assertions.assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        Assertions.assertFalse(tokenizer.hasNextToken());
    }

    @Test
    void testWithValidJack1() throws IOException {
        File inputFile = new File("TokenizerTests/jack_test_1.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("class", "JackTest", "{", "field", "int", "value",
                ";", "field", "Array", "arr", ";", "constructor", "new", "("
                , "int", "x", ",", "Array", "y", ")", "{", "let", "value", "=",
                "x", ";", "let", "arr", "=", "y", ";", "return", "this", ";"
                , "}", "}");
        for (int i = 0; i < expectedTokens.size(); i++) {
            Assertions.assertTrue(tokenizer.hasNextToken());
            Assertions.assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        Assertions.assertFalse(tokenizer.hasNextToken());
    }

    @Test
    void testJackCodeWithStringLiteral() throws IOException {
        File inputFile = new File("TokenizerTests/jack_test_string_lit.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("var", "String", "str",
                ";", "let", "str", "=", "\"hello world\"", ";");
        for (int i = 0; i < expectedTokens.size(); i++) {
            Assertions.assertTrue(tokenizer.hasNextToken());
            Assertions.assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        Assertions.assertFalse(tokenizer.hasNextToken());
    }

    @Test
    void testBlockComments() throws IOException {
        File inputFile = new File("TokenizerTests/test_block_comments.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("function", "myFunc", "("
                , ")", ";", "let", "do", "(", ")", ";", "\"mystring str\"");
        for (int i = 0; i < expectedTokens.size(); i++) {
            Assertions.assertTrue(tokenizer.hasNextToken());
            Assertions.assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        Assertions.assertFalse(tokenizer.hasNextToken());
    }

    @Test
    void testBlockComments2() throws IOException {
        File inputFile = new File("TokenizerTests/test_block_comments_2.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("class", "MyClass", "{",
            "field", "int", "myVar", ";", "function", "myFunc", "(", "int", "x", ")",
            "{", "do", "something", "(", ")", ";", "}", "}");
        for (int i = 0; i < expectedTokens.size(); i++) {
            Assertions.assertTrue(tokenizer.hasNextToken());
            Assertions.assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        Assertions.assertFalse(tokenizer.hasNextToken());
    }
}