import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private Parser parser;


    @Test
    void testClassVarDecOneVar() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_class_var_dec_one.txt"));
        // tests the declaration of a single variable
        String expected = "<keyword>field</keyword>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>;</symbol>\n";
        assertEquals(expected, parser.compileClassVarDec());
    }

    @Test
    void testClassVarDecMultiple() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_class_var_dec_mult.txt"));
        // tests the declaration of a single variable
        String expected = "<keyword>static</keyword>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>myStr1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr3</identifier>\n" +
                "<symbol>;</symbol>\n";
        assertEquals(expected, parser.compileClassVarDec());
    }

    @Test
    void testParamListOne() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_param_list_one.txt"));
        // tests the declaration of a single variable
        String expected = "<symbol>(</symbol>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>x</identifier>\n" +
                "<symbol>)</symbol>\n";
        assertEquals(expected, parser.compileParamList());
    }

    @Test
    void testParamListMultiple() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_param_list_mult.txt"));
        // tests the declaration of a single variable
        String expected = "<symbol>(</symbol>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>x</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>y</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>z</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>)</symbol>\n";
        assertEquals(expected, parser.compileParamList());
    }

}