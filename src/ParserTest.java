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
        String expected = "<classVarDec>\n" +
                "<keyword>field</keyword>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</classVarDec>\n";
        assertEquals(expected, parser.compileClassVarDec());
    }

    @Test
    void testClassVarDecMultiple() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_class_var_dec_mult.txt"));
        // tests the declaration of a single variable
        String expected = "<classVarDec>\n" +
                "<keyword>static</keyword>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>myStr1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr3</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</classVarDec>\n";
        assertEquals(expected, parser.compileClassVarDec());
    }

    @Test
    void testParamListOne() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_param_list_one.txt"));
        // tests the declaration of a single variable
        String expected = "<symbol>(</symbol>\n" +
                "<parameterList>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>x</identifier>\n" +
                "</parameterList>\n" +
                "<symbol>)</symbol>\n";
        assertEquals(expected, parser.compileParamList());
    }

    @Test
    void testParamListMultiple() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_param_list_mult.txt"));
        // tests the declaration of a single variable
        String expected = "<symbol>(</symbol>\n" + "<parameterList>\n" +
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
                "</parameterList>\n" +
                "<symbol>)</symbol>\n";
        assertEquals(expected, parser.compileParamList());
    }

    @Test
    void testTermIntegerConstant() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_int_const.txt"));
        String expected = "<term>\n" +
                "<integerConstant>127374</integerConstant>\n" +
                "</term>\n";
        assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testTermStringConstant() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_string_const.txt"));
        String expected = "<term>\n" +
                "<StringConstant>hello world</StringConstant>\n" +
                "</term>\n";
        assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testTermUnaryOp() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_unary_op.txt"));
        String expected = "<term>\n" +
                "<symbol>~</symbol>\n" +
                "<term>\n" +
                    "<identifier>myBool</identifier>\n" +
                "</term>\n" +
                "</term>\n";
        assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testExpressionSingleTerm() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_expression_single_term.txt"));
        String expected = "<expression>\n" +
                "<term>\n" +
                "<identifier>myArray</identifier>\n" +
                "<symbol>[</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<integerConstant>10</integerConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>]</symbol>\n" +
                "</term>\n" +
                "</expression>\n";
        assertEquals(expected, parser.compileExpression());
    }

    @Test
    void testExpressionMultipleTerm() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_expression_multiple_term.txt"));
        String expected = "<expression>\n" +
                "<term>\n" +
                    "<identifier>myArr</identifier>\n" +
                    "<symbol>[</symbol>\n" +
                    "<expression>\n" +
                        "<term>\n" +
                        "<integerConstant>0</integerConstant>\n" +
                        "</term>\n" +
                    "</expression>\n" +
                    "<symbol>]</symbol>\n" +
                "</term>\n" +
                "<symbol>=</symbol>\n" +
                "<term>\n" +
                    "<identifier>Helper</identifier>\n" +
                    "<symbol>.</symbol>\n" +
                    "<identifier>myMethod</identifier>\n" +
                    "<symbol>(</symbol>\n" +
                    "<expressionList>\n" +
                        "<expression>\n" +
                            "<term>\n" +
                                "<StringConstant>hello</StringConstant>\n" +
                            "</term>\n" +
                        "</expression>\n" +
                        "<symbol>,</symbol>\n" +
                        "<expression>\n" +
                        "<term>\n" +
                            "<integerConstant>10</integerConstant>\n" +
                        "</term>\n" +
                        "</expression>\n" +
                    "</expressionList>\n" +
                    "<symbol>)</symbol>\n" +
                "</term>\n" +
                "</expression>\n";
        assertEquals(expected, parser.compileExpression());
    }

    @Test
    void testExpressionList() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_expression_list.txt"));
        String expected = "<expressionList>\n" +
                "<expression>\n" +
                    "<term>\n" +
                        "<keyword>true</keyword>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>,</symbol>\n" +
                "<expression>\n" +
                    "<term>\n" +
                        "<identifier>a</identifier>\n" +
                    "</term>\n" +
                    "<symbol>+</symbol>\n" +
                    "<term>\n" +
                        "<identifier>b</identifier>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>,</symbol>\n" +
                "<expression>\n" +
                    "<term>\n" +
                        "<identifier>myVar</identifier>\n" +
                        "<symbol>[</symbol>\n" +
                        "<expression>\n" +
                        "<term>\n" +
                            "<integerConstant>0</integerConstant>\n" +
                        "</term>\n" +
                        "</expression>\n" +
                        "<symbol>]</symbol>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>,</symbol>\n" +
                "<expression>\n" +
                    "<term>\n" +
                        "<identifier>c</identifier>\n" +
                    "</term>\n" +
                    "<symbol>=</symbol>\n" +
                    "<term>\n" +
                        "<symbol>~</symbol>\n" +
                        "<term>\n" +
                            "<identifier>d</identifier>\n" +
                        "</term>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "</expressionList>\n";
        assertEquals(expected, parser.compileExpressionList());
    }

    @Test
    void testReturnStatement() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_return_statement.txt"));
        String expected = "<expression>\n" +
                "<term>\n" +
                    "<identifier>myArr</identifier>\n" +
                    "<symbol>[</symbol>\n" +
                    "<expression>\n" +
                    "<term>\n" +
                        "<identifier>a</identifier>\n" +
                    "</term>\n" +
                    "<symbol>+</symbol>\n" +
                    "<term>\n" +
                        "<identifier>b</identifier>\n" +
                    "</term>\n" +
                    "</expression>\n" +
                    "<symbol>]</symbol>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</returnStatement>\n";
        assertEquals(expected, parser.compileReturnStatement());
    }

    @Test
    void testLetStatementNoExp() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_let_statement_no_exp.txt"));
        String expected = "<identifier>var1</identifier>\n" +
                "<symbol>=</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                    "<identifier>a</identifier>\n" +
                "</term>\n" +
                "<symbol>/</symbol>\n" +
                "<term>\n" +
                    "<identifier>b</identifier>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</letStatement>\n";
        assertEquals(expected, parser.compileLetStatement());
    }

    @Test
    void testLetStatementExp() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_let_statement_exp.txt"));
        String expected = "<identifier>var1</identifier>\n" +
                "<symbol>[</symbol>\n" +
                "<expression>\n" +
                    "<term>\n" +
                        "<identifier>counter</identifier>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>]</symbol>\n" +
                "<symbol>=</symbol>\n" +
                "<expression>\n" +
                    "<term>\n" +
                    "<identifier>a</identifier>\n" +
                    "</term>\n" +
                "<symbol>/</symbol>\n" +
                    "<term>\n" +
                    "<identifier>b</identifier>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</letStatement>\n";
        assertEquals(expected, parser.compileLetStatement());
    }

    @Test
    void testIfStatementNoElse() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_if_statement_no_else.txt"));
        String expected = "<symbol>(</symbol>\n" +
                "<expression>\n" +
                    "<term>\n" +
                        "<identifier>a</identifier>\n" +
                    "</term>\n" +
                    "<symbol>&lt;</symbol>\n" +
                    "<term>\n" +
                        "<identifier>myVar</identifier>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                    "<returnStatement>\n" +
                        "<keyword>return</keyword>\n" +
                        "<expression>\n" +
                            "<term>\n" +
                                "<identifier>a</identifier>\n" +
                            "</term>\n" +
                            "<symbol>+</symbol>\n" +
                            "<term>\n" +
                                "<integerConstant>1</integerConstant>\n" +
                            "</term>\n" +
                        "</expression>\n" +
                        "<symbol>;</symbol>\n" +
                    "</returnStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n";
        assertEquals(expected, parser.compileIfStatement());
    }

    @Test
    void testIfStatementWithElse() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_if_statement_with_else.txt"));
        String expected = "<symbol>(</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<identifier>a</identifier>\n" +
                "</term>\n" +
                "<symbol>&lt;</symbol>\n" +
                "<term>\n" +
                "<identifier>myVar</identifier>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                "<returnStatement>\n" +
                "<keyword>return</keyword>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<identifier>a</identifier>\n" +
                "</term>\n" +
                "<symbol>+</symbol>\n" +
                "<term>\n" +
                "<integerConstant>1</integerConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</returnStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "<keyword>else</keyword>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                    "<doStatement>\n" +
                        "<keyword>do</keyword>\n" +
                        "<identifier>subroutine</identifier>\n" +
                        "<symbol>.</symbol>\n" +
                        "<identifier>call</identifier>\n" +
                        "<symbol>(</symbol>\n" +
                            "<expressionList>\n" +
                            "</expressionList>\n" +
                        "<symbol>)</symbol>\n" +
                        "<symbol>;</symbol>\n" +
                    "</doStatement>\n" +
                    "<returnStatement>\n" +
                        "<keyword>return</keyword>\n" +
                        "<expression>\n" +
                            "<term>\n" +
                                "<integerConstant>10</integerConstant>\n" +
                            "</term>\n" +
                        "</expression>\n" +
                        "<symbol>;</symbol>\n" +
                    "</returnStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n";
        assertEquals(expected, parser.compileIfStatement());
    }

    @Test
    void testStatements1() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_statements_1.txt"));
        String expected = "<statements>\n" +
                "<ifStatement>\n" +
                    "<keyword>if</keyword>\n" +
                    "<symbol>(</symbol>\n" +
                        "<expression>\n" +
                        "<term>\n" +
                            "<identifier>myInt</identifier>\n" +
                        "</term>\n" +
                        "<symbol>=</symbol>\n" +
                        "<term>\n" +
                            "<integerConstant>10</integerConstant>\n" +
                        "</term>\n" +
                        "</expression>\n" +
                    "<symbol>)</symbol>\n" +
                    "<symbol>{</symbol>\n" +
                    "<statements>\n" +
                    "<doStatement>\n" +
                        "<keyword>do</keyword>\n" +
                        "<identifier>myMethod</identifier>\n" +
                        "<symbol>(</symbol>\n" +
                        "<expressionList>\n" +
                            "<expression>\n" +
                            "<term>\n" +
                                "<StringConstant>test</StringConstant>\n" +
                            "</term>\n" +
                            "</expression>\n" +
                            "<symbol>,</symbol>\n" +
                            "<expression>\n" +
                            "<term>\n" +
                                "<integerConstant>20</integerConstant>\n" +
                            "</term>\n" +
                            "</expression>\n" +
                        "</expressionList>\n" +
                        "<symbol>)</symbol>\n" +
                        "<symbol>;</symbol>\n" +
                "</doStatement>\n" +
                "<letStatement>\n" +
                    "<keyword>let</keyword>\n" +
                    "<identifier>myVar</identifier>\n" +
                    "<symbol>=</symbol>\n" +
                    "<expression>\n" +
                    "<term>\n" +
                        "<StringConstant>hello</StringConstant>\n" +
                    "</term>\n" +
                    "</expression>\n" +
                    "<symbol>;</symbol>\n" +
                "</letStatement>\n" +
                "<returnStatement>\n" +
                    "<keyword>return</keyword>\n" +
                    "<expression>\n" +
                    "<term>\n" +
                        "<identifier>myVar</identifier>\n" +
                    "</term>\n" +
                    "</expression>\n" +
                    "<symbol>;</symbol>\n" +
                "</returnStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n" +
                "</statements>\n";
                assertEquals(expected, parser.compileStatements());
    }

    @Test
    void testVarDecSingle() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_var_dec_single.txt"));
        String expected = "<varDec>\n" +
                "<keyword>var</keyword>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</varDec>\n";
        assertEquals(expected, parser.compileVarDec());
    }

    @Test
    void testVarDecMult() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_var_dec_mult.txt"));
        String expected = "<varDec>\n" +
                "<keyword>var</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>obj1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj3</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</varDec>\n";
        assertEquals(expected, parser.compileVarDec());
    }

    @Test
    void testSubroutineBodyNoVarDec() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_subroutine_body_no_var_dec.txt"));
        String expected = "<subroutineBody>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                    "<ifStatement>\n" +
                        "<keyword>if</keyword>\n" +
                        "<symbol>(</symbol>\n" +
                        "<expression>\n" +
                            "<term>\n" +
                                "<identifier>myInt</identifier>\n" +
                            "</term>\n" +
                            "<symbol>=</symbol>\n" +
                            "<term>\n" +
                                "<integerConstant>10</integerConstant>\n" +
                            "</term>\n" +
                        "</expression>\n" +
                        "<symbol>)</symbol>\n" +
                        "<symbol>{</symbol>\n" +
                        "<statements>\n" +
                        "<doStatement>\n" +
                            "<keyword>do</keyword>\n" +
                            "<identifier>myMethod</identifier>\n" +
                            "<symbol>(</symbol>\n" +
                            "<expressionList>\n" +
                                "<expression>\n" +
                                "<term>\n" +
                                    "<StringConstant>test</StringConstant>\n" +
                                "</term>\n" +
                                "</expression>\n" +
                            "<symbol>,</symbol>\n" +
                                "<expression>\n" +
                                "<term>\n" +
                                    "<integerConstant>20</integerConstant>\n" +
                                "</term>\n" +
                                "</expression>\n" +
                            "</expressionList>\n" +
                            "<symbol>)</symbol>\n" +
                            "<symbol>;</symbol>\n" +
                        "</doStatement>\n" +
                    "<letStatement>\n" +
                        "<keyword>let</keyword>\n" +
                        "<identifier>myVar</identifier>\n" +
                        "<symbol>=</symbol>\n" +
                        "<expression>\n" +
                        "<term>\n" +
                            "<StringConstant>hello</StringConstant>\n" +
                        "</term>\n" +
                        "</expression>\n" +
                        "<symbol>;</symbol>\n" +
                    "</letStatement>\n" +
                    "<returnStatement>\n" +
                        "<keyword>return</keyword>\n" +
                        "<expression>\n" +
                        "<term>\n" +
                            "<identifier>myVar</identifier>\n" +
                        "</term>\n" +
                        "</expression>\n" +
                        "<symbol>;</symbol>\n" +
                    "</returnStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</subroutineBody>\n";
        assertEquals(expected, parser.compileSubroutineBody());
    }

    @Test
    void testSubroutineBodyWithVarDec() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_subroutine_body_with_var_dec.txt"));
        String expected = "<subroutineBody>\n" +
                "<symbol>{</symbol>\n" +
                "<varDec>\n" +
                    "<keyword>var</keyword>\n" +
                    "<identifier>MyClass</identifier>\n" +
                    "<identifier>obj1</identifier>\n" +
                    "<symbol>,</symbol>\n" +
                    "<identifier>obj2</identifier>\n" +
                    "<symbol>,</symbol>\n" +
                    "<identifier>obj3</identifier>\n" +
                    "<symbol>;</symbol>\n" +
                "</varDec>\n" +
                "<statements>\n" +
                "<ifStatement>\n" +
                "<keyword>if</keyword>\n" +
                "<symbol>(</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<identifier>myInt</identifier>\n" +
                "</term>\n" +
                "<symbol>=</symbol>\n" +
                "<term>\n" +
                "<integerConstant>10</integerConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                "<doStatement>\n" +
                "<keyword>do</keyword>\n" +
                "<identifier>myMethod</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<expressionList>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<StringConstant>test</StringConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>,</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<integerConstant>20</integerConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "</expressionList>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>;</symbol>\n" +
                "</doStatement>\n" +
                "<letStatement>\n" +
                "<keyword>let</keyword>\n" +
                "<identifier>myVar</identifier>\n" +
                "<symbol>=</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<StringConstant>hello</StringConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</letStatement>\n" +
                "<returnStatement>\n" +
                "<keyword>return</keyword>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<identifier>myVar</identifier>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</returnStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</subroutineBody>\n";
        assertEquals(expected, parser.compileSubroutineBody());
    }

    @Test
    void testSubroutineDec() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_subroutine_dec.txt"));
        String expected = "<subroutineDec>\n" +
                "<keyword>function</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>myFunc</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<parameterList>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>num1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>str1</identifier>\n" +
                "</parameterList>\n" +
                "<symbol>)</symbol>\n" +
                "<subroutineBody>\n" +
                "<symbol>{</symbol>\n" +
                "<varDec>\n" +
                "<keyword>var</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>obj1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj3</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</varDec>\n" +
                "<statements>\n" +
                "<ifStatement>\n" +
                "<keyword>if</keyword>\n" +
                "<symbol>(</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<identifier>myInt</identifier>\n" +
                "</term>\n" +
                "<symbol>=</symbol>\n" +
                "<term>\n" +
                "<integerConstant>10</integerConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                "<doStatement>\n" +
                "<keyword>do</keyword>\n" +
                "<identifier>myMethod</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<expressionList>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<StringConstant>test</StringConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>,</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<integerConstant>20</integerConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "</expressionList>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>;</symbol>\n" +
                "</doStatement>\n" +
                "<letStatement>\n" +
                "<keyword>let</keyword>\n" +
                "<identifier>myVar</identifier>\n" +
                "<symbol>=</symbol>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<StringConstant>hello</StringConstant>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</letStatement>\n" +
                "<returnStatement>\n" +
                "<keyword>return</keyword>\n" +
                "<expression>\n" +
                "<term>\n" +
                "<identifier>myVar</identifier>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>;</symbol>\n" +
                "</returnStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</subroutineBody>\n" +
                "</subroutineDec>\n";
        assertEquals(expected, parser.compileSubroutineDec());
    }

    @Test
    void testClassDecNoClassVar() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_class_dec_no_class_var.txt"));
        String expected = "<class>\n" +
                "<keyword>class</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<symbol>{</symbol>\n" +
                "<subroutineDec>\n" +
                "<keyword>function</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>myFunc</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<parameterList>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>num1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>str1</identifier>\n" +
                "</parameterList>\n" +
                "<symbol>)</symbol>\n" +
                "<subroutineBody>\n" +
                "<symbol>{</symbol>\n" +
                "<keyword>var</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>obj1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj3</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "<keyword>if</keyword>\n" +
                "<symbol>(</symbol>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>=</symbol>\n" +
                "<integerConstant>10</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<keyword>do</keyword>\n" +
                "<identifier>myMethod</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<StringConstant>test</StringConstant>\n" +
                "<symbol>,</symbol>\n" +
                "<integerConstant>20</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>;</symbol>\n" +
                "<keyword>let</keyword>\n" +
                "<identifier>myVar</identifier>\n" +
                "<symbol>=</symbol>\n" +
                "<StringConstant>hello</StringConstant>\n" +
                "<symbol>;</symbol>\n" +
                "<keyword>return</keyword>\n" +
                "<identifier>myVar</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "<symbol>}</symbol>\n" +
                "<symbol>}</symbol>\n" +
                "</subroutineBody>\n" +
                "</subroutineDec>\n" +
                "<symbol>}</symbol>\n" +
                "</class>\n";
        assertEquals(expected, parser.compileClass());
    }

    @Test
    void testClassDecWithClassVar() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_class_dec_with_class_var.txt"));
        String expected = "<class>\n" +
                "<keyword>class</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<symbol>{</symbol>\n" +
                "<classVarDec>\n" +
                "<keyword>static</keyword>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>myStr1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr3</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</classVarDec>\n" +
                "<classVarDec>\n" +
                "<keyword>field</keyword>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</classVarDec>\n" +
                "<subroutineDec>\n" +
                "<keyword>function</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>myFunc</identifier>\n" +
                "<parameterList>\n" +
                "<symbol>(</symbol>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>num1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>str1</identifier>\n" +
                "<symbol>)</symbol>\n" +
                "</parameterList>\n" +
                "<subroutineBody>\n" +
                "<symbol>{</symbol>\n" +
                "<keyword>var</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<identifier>obj1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>obj3</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "<keyword>if</keyword>\n" +
                "<symbol>(</symbol>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>=</symbol>\n" +
                "<integerConstant>10</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<keyword>do</keyword>\n" +
                "<identifier>myMethod</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<StringConstant>test</StringConstant>\n" +
                "<symbol>,</symbol>\n" +
                "<integerConstant>20</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>;</symbol>\n" +
                "<keyword>let</keyword>\n" +
                "<identifier>myVar</identifier>\n" +
                "<symbol>=</symbol>\n" +
                "<StringConstant>hello</StringConstant>\n" +
                "<symbol>;</symbol>\n" +
                "<keyword>return</keyword>\n" +
                "<identifier>myVar</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "<symbol>}</symbol>\n" +
                "<symbol>}</symbol>\n" +
                "</subroutineBody>\n" +
                "</subroutineDec>\n" +
                "<symbol>}</symbol>\n" +
                "</class>\n";
        assertEquals(expected, parser.compileClass());
    }

    @Test
    void testClassDecWithSubroutine() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_class_dec_no_subroutine.txt"));
        String expected = "<class>\n" +
                "<keyword>class</keyword>\n" +
                "<identifier>MyClass</identifier>\n" +
                "<symbol>{</symbol>\n" +
                "<classVarDec>\n" +
                "<keyword>static</keyword>\n" +
                "<identifier>String</identifier>\n" +
                "<identifier>myStr1</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr2</identifier>\n" +
                "<symbol>,</symbol>\n" +
                "<identifier>myStr3</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</classVarDec>\n" +
                "<classVarDec>\n" +
                "<keyword>field</keyword>\n" +
                "<keyword>int</keyword>\n" +
                "<identifier>myInt</identifier>\n" +
                "<symbol>;</symbol>\n" +
                "</classVarDec>\n" +
                "<symbol>}</symbol>\n" +
                "</class>\n";
        assertEquals(expected, parser.compileClass());
    }

    @Test
    void testNestedExpressions() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_nested_expressions.txt"));
        String expected = "<keyword>if</keyword>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<identifier>y</identifier>\n" +
                "<symbol>+</symbol>\n" +
                "<identifier>size</identifier>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol><</symbol>\n" +
                "<integerConstant>254</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>&</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<identifier>x</identifier>\n" +
                "<symbol>+</symbol>\n" +
                "<identifier>size</identifier>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol><</symbol>\n" +
                "<integerConstant>510</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<keyword>do</keyword>\n" +
                "<identifier>erase</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>;</symbol>\n" +
                "<symbol>}</symbol>\n";
        assertEquals(expected, parser.compileStatements());
    }

    @Test
    void testNestedExpression2() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_nested_expressions_2.txt"));
        String expected = "<keyword>if</keyword>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<identifier>y</identifier>\n" +
                "<symbol>+</symbol>\n" +
                "<identifier>size</identifier>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol><</symbol>\n" +
                "<integerConstant>254</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>&</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>(</symbol>\n" +
                "<identifier>x</identifier>\n" +
                "<symbol>+</symbol>\n" +
                "<identifier>size</identifier>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>/</symbol>\n" +
                "<integerConstant>5</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol><</symbol>\n" +
                "<integerConstant>510</integerConstant>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<keyword>do</keyword>\n" +
                "<identifier>erase</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>;</symbol>\n" +
                "<symbol>}</symbol>\n";
    }

    @Test
    void testEmptyParamsList() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_empty_params_list.txt"));
        String expected = "<parameterList>\n" +
                "<symbol>(</symbol>\n" +
                "<symbol>)</symbol>\n" +
                "</parameterList>\n";
        assertEquals(expected, parser.compileParamList());
    }
}