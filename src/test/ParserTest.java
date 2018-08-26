package test;

import main.ExpressionList;
import main.Parser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import symbol.Symbol;
import symbol.SymbolKind;
import symboltable.ClassSymbolTable;
import symboltable.SubroutineSymbolTable;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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
        Assertions.assertEquals(expected, parser.compileClassVarDec());
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
        Assertions.assertEquals(expected, parser.compileClassVarDec());
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
        Assertions.assertEquals(expected, parser.compileParamList());
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
        Assertions.assertEquals(expected, parser.compileParamList());
    }

    @Test
    void testTermIntegerConstant() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_int_const.txt"));
        String expected = "push constant 12733\n";
        Assertions.assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testTermStringConstant() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_string_const.txt"));
        String expected = "push constant 11\n" +
                "call String.new 1\n" +
                "push constant 104\n" + // h
                "call String.appendChar 2\n" +
                "push constant 101\n" + // e
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 111\n" + // o
                "call String.appendChar 2\n" +
                "push constant 32\n" + // space
                "call String.appendChar 2\n" +
                "push constant 119\n" + // w
                "call String.appendChar 2\n" +
                "push constant 111\n" + // o
                "call String.appendChar 2\n" +
                "push constant 114\n" + // r
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 100\n" + // d
                "call String.appendChar 2\n";
        Assertions.assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testTermUnaryOp() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_unary_op.txt"));
        SubroutineSymbolTable st = parser.getSubroutineST();
        st.define("myBool", "boolean", SymbolKind.LOCAL);
        String expected = "push local 0\n" +
                "not\n";
        Assertions.assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testExpressionSingleTerm() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_expression_single_term.txt"));
        ClassSymbolTable classST = parser.getClassST();
        classST.define("myArray", "Array", SymbolKind.FIELD);
        String expected = "push this 0\n" +
                "push constant 10\n" +
                "add\n" +
                "pop pointer 1\n" +
                "push that 0\n";

        Assertions.assertEquals(expected, parser.compileExpression());
    }

    @Test
    void testExpressionMultipleTerms() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_expression_multiple_term.txt"));
        ClassSymbolTable classST = parser.getClassST();
        classST.define("myArr", "Array", SymbolKind.FIELD);

        String expected = "push this 0\n" +
                "push constant 0\n" +
                "add\n" +
                "pop pointer 1\n" +
                "push that 0\n" +
                "push constant 5\n" + // new string literal
                "call String.new 1\n" +
                "push constant 104\n" + // h
                "call String.appendChar 2\n" +
                "push constant 101\n" + // e
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 111\n" + // o
                "call String.appendChar 2\n" +
                "push constant 10\n" +
                "call Helper.myMethod 2\n" +
                "eq\n";
        Assertions.assertEquals(expected, parser.compileExpression());
    }

    @Test
    void testExpressionList() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_expression_list.txt"));
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        classST.define("a", "int", SymbolKind.FIELD);
        subroutineST.define("b", "int", SymbolKind.ARGUMENT);

        subroutineST.define("myVar", "Array", SymbolKind.LOCAL);
        classST.define("c", "int", SymbolKind.STATIC);
        classST.define("d", "boolean", SymbolKind.STATIC);
        String expected = "push constant 1\n" +
                "neg\n" + // push true
                "push this 0\n" + // a
                "push argument 0\n" + // b
                "add\n" +
                "push local 0\n" + // myVar[0]
                "push constant 0\n" +
                "add\n" +
                "pop pointer 1\n" +
                "push that 0\n" +
                "push static 0\n" + // c
                "push static 1\n" + // d
                "not\n" + // ~
                "eq\n"; // =
        ExpressionList expList = parser.compileExpressionList();
        Assertions.assertEquals(expected, expList.getVmCode());
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
        Assertions.assertEquals(expected, parser.compileReturnStatement());
    }

    @Test
    void testLetStatementNoExp() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_let_statement_no_exp.txt"));
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        classST.define("var1", "int", SymbolKind.FIELD);
        subroutineST.define("a", "int", SymbolKind.LOCAL);
        subroutineST.define("b", "int", SymbolKind.LOCAL);
        String expected = "push local 0\n" +
                "push local 1\n" +
                "call Math.divide 2\n" +
                "pop this 0\n";
        Assertions.assertEquals(expected, parser.compileLetStatement());
    }

    @Test
    void testLetStatementExp() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_let_statement_exp.txt"));
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();

        classST.define("var1", "Array", SymbolKind.FIELD);
        subroutineST.define("counter", "int", SymbolKind.LOCAL);
        subroutineST.define("a", "int", SymbolKind.LOCAL);
        subroutineST.define("b", "int", SymbolKind.LOCAL);

        String expected = "push this 0\n" +
                "push local 0\n" +
                "add\n" +
                "push local 1\n" +
                "push local 2\n" +
                "call Math.divide 2\n" +
                "pop temp 0\n" +
                "pop pointer 1\n" +
                "push temp 0\n" +
                "pop that 0\n";
        Assertions.assertEquals(expected, parser.compileLetStatement());
    }

    @Test
    void testLetStatementArrayBothSides() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_let_statement_array_both_sides.txt"));
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();

        classST.define("var1", "Array", SymbolKind.FIELD);
        classST.define("arr", "Array", SymbolKind.STATIC);
        subroutineST.define("counter", "int", SymbolKind.LOCAL);
        subroutineST.define("a", "int", SymbolKind.LOCAL);
        subroutineST.define("b", "int", SymbolKind.LOCAL);

        String expected = "push this 0\n" +
                "push local 0\n" + // counter
                "push constant 5\n" +
                "add\n" +
                "add\n" +
                "push static 0\n" +
                "push local 1\n" +
                "push local 2\n" +
                "call Math.divide 2\n" +
                "add\n" +
                "pop pointer 1\n" +
                "push that 0\n" +
                "pop temp 0\n" +
                "pop pointer 1\n" +
                "push temp 0\n" +
                "pop that 0\n";
        Assertions.assertEquals(expected, parser.compileLetStatement());
    }

    @Test
    void testIfStatementNoElse() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_if_statement_no_else.txt"));
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        ClassSymbolTable classST = parser.getClassST();
        subroutineST.define("a", "int", SymbolKind.LOCAL);
        subroutineST.define("myVar", "int", SymbolKind.LOCAL);

        String parsed = parser.compileIfStatement();
        int currNumLabel = Parser.getNumLabels();
        String expected = "push local 0\n" +
                "push local 1\n" +
                "lt\n" +
                "not\n" +
                String.format("if-goto LBL_%d\n", currNumLabel - 2) +
                "push local 0\n" +
                "push constant 1\n" +
                "add\n" +
                "return\n" +
                String.format("goto LBL_%d\n", currNumLabel - 1) +
                String.format("label LBL_%d\n", currNumLabel - 2) +
                String.format("label LBL_%d\n", currNumLabel - 1);

        Assertions.assertEquals(expected, parsed);
    }

    @Test
    void testIfStatementWithElse() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_if_statement_with_else.txt"));
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        ClassSymbolTable classST = parser.getClassST();
        subroutineST.define("a", "int", SymbolKind.LOCAL);
        subroutineST.define("myVar", "int", SymbolKind.LOCAL);

        parser.setCurrentClassName("SomeClass");
        String parsed = parser.compileIfStatement();
        int currNumLabel = Parser.getNumLabels();
        String expected = "push local 0\n" +
                "push local 1\n" +
                "lt\n" +
                "not\n" +
                String.format("if-goto LBL_%d\n", currNumLabel - 2) +
                "push local 0\n" +
                "push constant 1\n" +
                "add\n" +
                "return\n" +
                String.format("goto LBL_%d\n", currNumLabel - 1) +
                String.format("label LBL_%d\n", currNumLabel - 2) +
                "push pointer 0\n" +
                "call SomeClass.subroutine_call 1\n" +
                "pop temp 0\n" +
                "push constant 10\n" +
                "return\n" +
                String.format("label LBL_%d\n", currNumLabel - 1);
        Assertions.assertEquals(expected, parsed);
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
                                "<stringConstant>test</stringConstant>\n" +
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
                        "<stringConstant>hello</stringConstant>\n" +
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
                Assertions.assertEquals(expected, parser.compileStatements());
    }

    @Test
    void testVarDecSingle() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_var_dec_single.txt"));
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        parser.compileVarDec();

        Optional<Symbol> boxedSymbol = subroutineST.lookUp("myInt");
        assertTrue(boxedSymbol.isPresent());
        assertEquals("int", boxedSymbol.get().getDataType());
        assertEquals(0, boxedSymbol.get().getNumKind());
    }

    @Test
    void testVarDecMult() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_var_dec_mult.txt"));
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        parser.compileVarDec();

        String[] varNames = {"obj1", "obj2", "obj3"};
        for (int i = 0; i < varNames.length; i++) {
            Optional<Symbol> boxedSymbol = subroutineST.lookUp(varNames[i]);
            assertTrue(boxedSymbol.isPresent());
            assertEquals("MyClass", boxedSymbol.get().getDataType());
            assertEquals(i, boxedSymbol.get().getNumKind());
        }
    }

    @Test
    void testSubroutineBodyNoVarDec() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_subroutine_body_no_var_dec.txt"));
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        parser.setCurrentClassName("MyClass");


        classST.define("myInt", "int", SymbolKind.FIELD);
        classST.define("myVar", "String", SymbolKind.FIELD);

        String parsed = parser.compileSubroutineBody();
        int numLabels = Parser.getNumLabels();

        String expected = "push this 0\n" +
                "push constant 10\n" +
                "eq\n" +
                "not\n" +
                String.format("if-goto LBL_%d\n", numLabels - 2) +
                "push pointer 0\n" +
                "push constant 4\n" + // "test" string literal
                "call String.new 1\n" +
                "push constant 116\n" +
                "call String.appendChar 2\n" +
                "push constant 101\n" +
                "call String.appendChar 2\n" +
                "push constant 115\n" +
                "call String.appendChar 2\n" +
                "push constant 116\n" +
                "call String.appendChar 2\n" +
                "push constant 20\n" +
                "call MyClass.myMethod 3\n" +
                "pop temp 0\n" + // pop for void method
                "push constant 5\n" + // "hello" string literal
                "call String.new 1\n" +
                "push constant 104\n" + // H
                "call String.appendChar 2\n" +
                "push constant 101\n" + // e
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 111\n" + // o
                "call String.appendChar 2\n" +
                "pop this 1\n" +
                "push this 1\n" +
                "return\n" +
                String.format("goto LBL_%d\n", numLabels - 1) +
                String.format("label LBL_%d\n", numLabels - 2) +
                String.format("label LBL_%d\n", numLabels - 1);

        Assertions.assertEquals(expected, parsed);
    }

    @Test
    void testSubroutineBodyWithVarDec() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_subroutine_body_with_var_dec.txt"));
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();
        parser.setCurrentClassName("MyClass");
        classST.define("myInt", "int", SymbolKind.FIELD);
        classST.define("myVar", "String", SymbolKind.FIELD);

        String parsed = parser.compileSubroutineBody();
        int numLabels = Parser.getNumLabels();
        String expected = "push this 0\n" +
                "push constant 10\n" +
                "eq\n" +
                "not\n" +
                String.format("if-goto LBL_%d\n", numLabels - 2) +
                "push pointer 0\n" +
                "push constant 4\n" + // "test" string literal
                "call String.new 1\n" +
                "push constant 116\n" +
                "call String.appendChar 2\n" +
                "push constant 101\n" +
                "call String.appendChar 2\n" +
                "push constant 115\n" +
                "call String.appendChar 2\n" +
                "push constant 116\n" +
                "call String.appendChar 2\n" +
                "push constant 20\n" +
                "call MyClass.myMethod 3\n" +
                "pop temp 0\n" + // pop for void method
                "push constant 5\n" + // "hello" string literal
                "call String.new 1\n" +
                "push constant 104\n" + // H
                "call String.appendChar 2\n" +
                "push constant 101\n" + // e
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 111\n" + // o
                "call String.appendChar 2\n" +
                "pop this 1\n" +
                "push this 1\n" +
                "return\n" +
                String.format("goto LBL_%d\n", numLabels - 1) +
                String.format("label LBL_%d\n", numLabels - 2) +
                String.format("label LBL_%d\n", numLabels - 1);
        String[] varNames = {"obj1", "obj2", "obj3"};
        for (int i = 0; i < varNames.length; i++) {
            Optional<Symbol> boxedSymbol = subroutineST.lookUp(varNames[i]);
            assertTrue(boxedSymbol.isPresent());
            assertEquals("MyClass", boxedSymbol.get().getDataType());
            assertEquals(i, boxedSymbol.get().getNumKind());
        }
        Assertions.assertEquals(expected, parsed);
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
                "<stringConstant>test</stringConstant>\n" +
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
                "<stringConstant>hello</stringConstant>\n" +
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
        Assertions.assertEquals(expected, parser.compileSubroutineDec());
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
                "<stringConstant>test</stringConstant>\n" +
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
                "<stringConstant>hello</stringConstant>\n" +
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
                "</subroutineDec>\n" +
                "<symbol>}</symbol>\n" +
                "</class>\n";
        Assertions.assertEquals(expected, parser.compileClass());
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
                "<stringConstant>test</stringConstant>\n" +
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
                "<stringConstant>hello</stringConstant>\n" +
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
                "</subroutineDec>\n" +
                "<symbol>}</symbol>\n" +
                "</class>\n";
        Assertions.assertEquals(expected, parser.compileClass());
    }

    @Test
    void testClassDecNoSubroutine() throws IOException {
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
        Assertions.assertEquals(expected, parser.compileClass());
    }

    @Test
    void testNestedExpressions() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_nested_expressions.txt"));
        String expected = "<statements>\n" +
                "<ifStatement>\n" +
                    "<keyword>if</keyword>\n" +
                    "<symbol>(</symbol>\n" +
                    "<expression>\n" +
                    "<term>\n" +
                        "<symbol>(</symbol>\n" +
                        "<expression>\n" +
                            "<term>\n" +
                                "<symbol>(</symbol>\n" +
                                "<expression>\n" +
                                    "<term>\n" +
                                    "<identifier>y</identifier>\n" +
                                    "</term>\n" +
                                    "<symbol>+</symbol>\n" +
                                    "<term>\n" +
                                    "<identifier>size</identifier>\n" +
                                    "</term>\n" +
                                "</expression>\n" +
                                "<symbol>)</symbol>\n" +
                            "</term>\n" +
                        "<symbol>&lt;</symbol>\n" +
                            "<term>\n" +
                                "<integerConstant>254</integerConstant>\n" +
                            "</term>\n" +
                        "</expression>\n" +
                    "<symbol>)</symbol>\n" +
                "</term>\n" +
                "<symbol>&amp;</symbol>\n" +
                "<term>\n" +
                    "<symbol>(</symbol>\n" +
                    "<expression>\n" +
                        "<term>\n" +
                            "<symbol>(</symbol>\n" +
                            "<expression>\n" +
                                "<term>\n" +
                                    "<identifier>x</identifier>\n" +
                                "</term>\n" +
                                "<symbol>+</symbol>\n" +
                                "<term>\n" +
                                    "<identifier>size</identifier>\n" +
                                "</term>\n" +
                            "</expression>\n" +
                            "<symbol>)</symbol>\n" +
                        "</term>\n" +
                    "<symbol>&lt;</symbol>\n" +
                    "<term>\n" +
                        "<integerConstant>510</integerConstant>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>)</symbol>\n" +
                "</term>\n" +
                "</expression>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                    "<doStatement>\n" +
                        "<keyword>do</keyword>\n" +
                        "<identifier>erase</identifier>\n" +
                        "<symbol>(</symbol>\n" +
                            "<expressionList>\n" +
                            "</expressionList>\n" +
                        "<symbol>)</symbol>\n" +
                        "<symbol>;</symbol>\n" +
                    "</doStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n" +
                "</statements>\n";
        Assertions.assertEquals(expected, parser.compileStatements());
    }

    @Test
    void testNestedExpression2() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_nested_expressions_2.txt"));
        String expected = "<statements>\n" +
                "<ifStatement>\n" +
                "<keyword>if</keyword>\n" +
                "<symbol>(</symbol>\n" +
                "<expression>\n" +
                    "<term>\n" +
                    "<symbol>(</symbol>\n" +
                    "<expression>\n" +
                        "<term>\n" +
                        "<symbol>(</symbol>\n" +
                        "<expression>\n" +
                            "<term>\n" +
                            "<identifier>y</identifier>\n" +
                            "</term>\n" +
                            "<symbol>+</symbol>\n" +
                            "<term>\n" +
                            "<identifier>size</identifier>\n" +
                            "</term>\n" +
                        "</expression>\n" +
                        "<symbol>)</symbol>\n" +
                        "</term>\n" +
                    "<symbol>&lt;</symbol>\n" +
                        "<term>\n" +
                        "<integerConstant>254</integerConstant>\n" +
                        "</term>\n" +
                    "</expression>\n" +
                    "<symbol>)</symbol>\n" +
                    "</term>\n" +
                    "<symbol>&amp;</symbol>\n" +
                    "<term>\n" +
                        "<symbol>(</symbol>\n" +
                        "<expression>\n" +
                        "<term>\n" +
                            "<symbol>(</symbol>\n" +
                            "<expression>\n" +
                                "<term>\n" +
                                    "<symbol>(</symbol>\n" +
                                        "<expression>\n" +
                                            "<term>\n" +
                                                "<identifier>x</identifier>\n" +
                                            "</term>\n" +
                                            "<symbol>+</symbol>\n" +
                                            "<term>\n" +
                                                "<identifier>size</identifier>\n" +
                                            "</term>\n" +
                                        "</expression>\n" +
                                    "<symbol>)</symbol>\n" +
                                "</term>\n" +
                                "<symbol>/</symbol>\n" +
                                "<term>\n" +
                                    "<integerConstant>5</integerConstant>\n" +
                                "</term>\n" +
                            "</expression>\n" +
                            "<symbol>)</symbol>\n" +
                        "</term>\n" +
                        "<symbol>&lt;</symbol>\n" +
                        "<term>\n" +
                        "<integerConstant>510</integerConstant>\n" +
                        "</term>\n" +
                        "</expression>\n" +
                        "<symbol>)</symbol>\n" +
                    "</term>\n" +
                "</expression>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>{</symbol>\n" +
                "<statements>\n" +
                "<doStatement>\n" +
                "<keyword>do</keyword>\n" +
                "<identifier>erase</identifier>\n" +
                "<symbol>(</symbol>\n" +
                "<expressionList>\n" +
                "</expressionList>\n" +
                "<symbol>)</symbol>\n" +
                "<symbol>;</symbol>\n" +
                "</doStatement>\n" +
                "</statements>\n" +
                "<symbol>}</symbol>\n" +
                "</ifStatement>\n" +
                "</statements>\n";
        Assertions.assertEquals(expected, parser.compileStatements());
    }

    @Test
    void testEmptyParamsList() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_empty_params_list.txt"));
        String expected = "<symbol>(</symbol>\n" +
                "<parameterList>\n" +
                "</parameterList>\n" +
                "<symbol>)</symbol>\n";
        Assertions.assertEquals(expected, parser.compileParamList());
    }

    @Test
    void testEmptyExpressionList() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_empty_expression_list.txt"));
        String expected = "";
        ExpressionList expList = parser.compileExpressionList();
        Assertions.assertEquals(expected, expList.getVmCode());
    }

    @Test
    void testStringConstantWithSymbols1() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_string_const_symbol_1.txt"));
        String expected = "push constant 17\n" +
                "call String.new 1\n" +
                "push constant 69\n" +
                "call String.appendChar 2\n" +
                "push constant 78\n" +
                "call String.appendChar 2\n" +
                "push constant 84\n" +
                "call String.appendChar 2\n" +
                "push constant 69\n" +
                "call String.appendChar 2\n" +
                "push constant 82\n" +
                "call String.appendChar 2\n" +
                "push constant 32\n" +
                "call String.appendChar 2\n" +
                "push constant 89\n" +
                "call String.appendChar 2\n" +
                "push constant 79\n" +
                "call String.appendChar 2\n" +
                "push constant 85\n" +
                "call String.appendChar 2\n" +
                "push constant 82\n" +
                "call String.appendChar 2\n" +
                "push constant 32\n" +
                "call String.appendChar 2\n" +
                "push constant 78\n" +
                "call String.appendChar 2\n" +
                "push constant 65\n" +
                "call String.appendChar 2\n" +
                "push constant 77\n" +
                "call String.appendChar 2\n" +
                "push constant 69\n" +
                "call String.appendChar 2\n" +
                "push constant 58\n" +
                "call String.appendChar 2\n" +
                "push constant 32\n" +
                "call String.appendChar 2\n";
        Assertions.assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testTermSubroutineCall() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_subroutine_call.txt"));
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();

        classST.define("myObj", "Object", SymbolKind.FIELD);
        subroutineST.define("myArr", "Array", SymbolKind.LOCAL);
        classST.define("a", "boolean", SymbolKind.STATIC);
        subroutineST.define("b", "int", SymbolKind.ARGUMENT);

        String expected = "push this 0\n" + // myObj
                "push local 0\n" + // myArr
                "push constant 0\n" +
                "add\n" +
                "pop pointer 1\n" +
                "push that 0\n" +
                "push static 0\n" + // a
                "not\n" +
                "push argument 0\n" +
                "call Object.some_method 4\n";
        assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testTermSubroutineCallSameClass() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_term_subroutine_call_same_class.txt"));
        parser.setCurrentClassName("SomeClass");
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();

        subroutineST.define("myArr", "Array", SymbolKind.LOCAL);
        classST.define("a", "boolean", SymbolKind.STATIC);
        subroutineST.define("b", "int", SymbolKind.ARGUMENT);

        String expected = "push pointer 0\n" + // this object
                "push local 0\n" + // myArr
                "push constant 0\n" +
                "add\n" +
                "pop pointer 1\n" +
                "push that 0\n" +
                "push static 0\n" + // a
                "not\n" +
                "push argument 0\n" +
                "call SomeClass.some_method 4\n";
        assertEquals(expected, parser.compileTerm());
    }

    @Test
    void testWhileStatement() throws IOException {
        this.parser = new Parser(new File("ParserTests/test_while_statement.txt"));
        parser.setCurrentClassName("SomeClass");
        ClassSymbolTable classST = parser.getClassST();
        SubroutineSymbolTable subroutineST = parser.getSubroutineST();

        subroutineST.define("myObj", "Object", SymbolKind.LOCAL);
        classST.define("a", "boolean", SymbolKind.STATIC);
        subroutineST.define("b", "boolean", SymbolKind.ARGUMENT);

        String parsed = parser.compileWhileStatement();
        int numLabels = Parser.getNumLabels();
        String expected = String.format("label LBL_%d\n", numLabels - 2) +
                "push local 0\n" +
                "push static 0\n" +
                "push argument 0\n" +
                "and\n" +
                "call Object.check 2\n" +
                "not\n" +
                String.format("if-goto LBL_%d\n", numLabels - 1) +
                "push pointer 0\n" +
                "call SomeClass.something 1\n" +
                "pop temp 0\n" +
                String.format("goto LBL_0\n", numLabels - 2) +
                String.format("label LBL_%d\n", numLabels - 1);
        assertEquals(expected, parsed);
    }
}