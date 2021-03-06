package main;

/**
 * Class responsible for generating VM code
 */
public class CodeGenerator {

    /**
     * Generates a VM push instruction
     * @param segment the memory segment to push from
     * @param index the index of the memory segment to push from
     * @return a VM push instruction
     */
    public String generatePush(MemorySegment segment, int index) {
        return String.format("push %s %d\n", segment.name().toLowerCase(), index);
    }

    /**
     * Generates a VM pop instruction
     * @param segment the memory segment to pop to
     * @param index the index of the memory segment to pop to
     * @return a VM pop instruction
     */
    public String generatePop(MemorySegment segment, int index) {
        return String.format("pop %s %d\n", segment.name().toLowerCase(), index);
    }

    /**
     * Generates an arithmetic/logical VM instruction
     * @param command the command
     * @return an arithmetic/logical VM instruction
     */
    public String generateArithLogical(String command) {
        String VMOp = "";
        switch (command) {
            case "+":
                VMOp = "add";
                break;
            case "-":
                VMOp = "sub";
                break;
            case "=":
                VMOp = "eq";
                break;
            case ">":
                VMOp = "gt";
                break;
            case "<":
                VMOp = "lt";
                break;
            case "&":
                VMOp = "and";
                break;
            case "|":
                VMOp = "or";
                break;
            case "*":
                VMOp = "call Math.multiply 2";
                break;
            case "/":
                VMOp = "call Math.divide 2";
                break;
        }
        return VMOp + "\n";
    }

    /**
     * Generate the VM command for a unary operation
     * @param op the operation
     * @return the VM command for a unary operation
     */
    public String generateUnaryOp(String op) {
        String VMOp = "";
        switch (op) {
            case "-":
                VMOp = "neg";
                break;
            case "~":
                VMOp = "not";
                break;
        }
        return VMOp + "\n";
    }

    /**
     * Generates a label in VM code
     * @param label the label to generate
     * @return a label in VM code
     */
    public String generateLabel(String label) {
        return String.format("label %s\n", label);
    }

    /**
     * Generates a goto command in VM code
     * @param label the label to goto
     * @return a goto command in VM code
     */
    public String generateGOTO(String label) {
        return String.format("goto %s\n", label);
    }

    /**
     * Generates an if-goto command in VM code
     * @param label the label to goto
     * @return an if-goto command in VM code
     */
    public String generateIfGOTO(String label) {
        return String.format("if-goto %s\n", label);
    }

    /**
     * Generates a function call in VM code
     * @param funcName name of the function
     * @param numArgs number of arguments the function takes
     * @return a function call in VM code
     */
    public String generateFuncCall(String funcName, int numArgs) {
        return String.format("call %s %d\n", funcName, numArgs);
    }

    /**
     * Generates a function definition in VM code
     * @param funcName name of the function
     * @param numLocals number of local variables the function has
     * @return a function definition in VM code
     */
    public String generateFunction(String funcName, int numLocals) {
        return String.format("function %s %d\n", funcName, numLocals);
    }

    /**
     * Generates a return statement in VM code
     * @return a return statement in VM code
     */
    public String generateReturn() {
        return "return\n";
    }

    /**
     * Generates the VM code for constructing a string literal
     * @param str the string literal
     * @return the VM code for constructing a string literal
     */
    public String generateStringLiteral(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(generatePush(MemorySegment.CONSTANT, str.length()));
        sb.append(generateFuncCall("String.new", 1));
        for (int i = 0; i < str.length(); i++) {
            int asciiOfChar = (int) str.charAt(i);
            sb.append(generatePush(MemorySegment.CONSTANT, asciiOfChar));
            sb.append(generateFuncCall("String.appendChar", 2));
        }
        return sb.toString();
    }

    /**
     * Generates the VM code for pushing a keyword constant (e.g. true, false, null, etc)
     * @param keyword the keyword
     * @return the VM code for pushing a keyword constant
     */
    public String generateKeywordConstant(String keyword) {
        String vmCode = "";
        switch (keyword) {
            case "true":
                vmCode += generatePush(MemorySegment.CONSTANT, 1);
                vmCode += generateUnaryOp("-");
                break;
            case "false": case "null":
                vmCode += generatePush(MemorySegment.CONSTANT, 0);
                break;
            case "this":
                vmCode += generatePush(MemorySegment.POINTER, 0);
                break;
        }
        return vmCode;
    }
}
