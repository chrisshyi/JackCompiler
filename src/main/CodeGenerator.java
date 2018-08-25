package main;

/**
 * Class responsible for generating VM code
 */
public class CodeGenerator {

    private final String pushTemplate = "push %s %d\n";
    private final String popTemplate = "pop %s %d\n";

    /**
     * Generates a VM push instruction
     * @param segment the memory segment to push from
     * @param index the index of the memory segment to push from
     * @return a VM push instruction
     */
    public String generatePush(MemorySegment segment, int index) {
        return String.format(pushTemplate, segment.name().toLowerCase(), index);
    }

    /**
     * Generates a VM pop instruction
     * @param segment the memory segment to pop to
     * @param index the index of the memory segment to pop to
     * @return a VM pop instruction
     */
    public String generatePop(MemorySegment segment, int index) {
        return String.format(popTemplate, segment.name().toLowerCase(), index);
    }

    /**
     * Generates an arithmetic/logical VM instruction
     * @param command the command
     * @return an arithmetic/logical VM instruction
     */
    public String generateArithLogical(String command) {
        String toWrite = "";
        switch (command) {
            case "+":
                toWrite = "add";
                break;
            case "-":
                toWrite = "sub";
                break;
            case "~":
                toWrite = "neg";
                break;
            case "=":
                toWrite = "eq";
                break;
            case ">":
                toWrite = "gt";
                break;
            case "<":
                toWrite = "lt";
                break;
            case "&":
                toWrite = "and";
                break;
            case "|":
                toWrite = "or";
                break;
            case "!":
                toWrite = "not";
                break;
            case "*":
                toWrite = "call Math.multiply 2";
                break;
            case "/":
                toWrite = "call Math.divide 2";
                break;
        }
        return toWrite + "\n";
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
}
