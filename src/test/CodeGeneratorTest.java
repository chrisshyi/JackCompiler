package test;

import main.CodeGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CodeGeneratorTest {

    CodeGenerator generator;

    @Test
    void testStringConstant() {
        generator = new CodeGenerator();
        String str = "Hello";
        String expected = "push constant 5\n" +
                "call String.new 1\n" +
                "push constant 72\n" + // H
                "call String.appendChar 2\n" +
                "push constant 101\n" + // e
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 108\n" + // l
                "call String.appendChar 2\n" +
                "push constant 111\n" + // o
                "call String.appendChar 2\n";
        assertEquals(expected, generator.generateStringLiteral(str));
    }
}
