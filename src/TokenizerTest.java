import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void testWithCommentLine() throws IOException {
        File inputFile = new File("test1.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("these", "are", "valid", "tokens");
        for (int i = 0; i < expectedTokens.size(); i++) {
            assertTrue(tokenizer.hasNextToken());
            assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        assertFalse(tokenizer.hasNextToken());
    }

    @Test
    void testWithInlineComment() throws IOException {
        File inputFile = new File("test2.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("half", "(", "of", ")", "this", "line", "are", "comments");
        for (int i = 0; i < expectedTokens.size(); i++) {
            assertTrue(tokenizer.hasNextToken());
            assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        assertFalse(tokenizer.hasNextToken());
    }

    @Test
    void testWithValidJack1() throws IOException {
        File inputFile = new File("jack_test_1.txt");
        Tokenizer tokenizer = new Tokenizer(inputFile);
        List<String> expectedTokens = Arrays.asList("class", "JackTest", "{", "field", "int", "value",
                ";", "field", "Array", "arr", ";", "constructor", "new", "("
                , "int", "x", ",", "Array", "y", ")", "{", "let", "value", "=",
                "x", ";", "let", "arr", "=", "y", ";", "return", "this", ";"
                , "}", "}");
        for (int i = 0; i < expectedTokens.size(); i++) {
            assertTrue(tokenizer.hasNextToken());
            assertEquals(expectedTokens.get(i), tokenizer.getNextToken());
        }
        assertFalse(tokenizer.hasNextToken());
    }
}