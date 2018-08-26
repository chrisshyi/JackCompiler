package main;

import symbol.Symbol;
import symbol.SymbolKind;
import symboltable.ClassSymbolTable;
import symboltable.SubroutineSymbolTable;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Works in tandem with a main.Tokenizer object to generate a parsetree
 */
public class Parser {
    //TODO: need to handle void subroutine definition and calling
    private Tokenizer tokenizer;
    private String outputFilePath;
    private SubroutineSymbolTable subroutineST;
    private ClassSymbolTable classST;
    private CodeGenerator codeGenerator;
    private String currentClassName = ""; // name of the class being compiled
    private static int numLabels = 0; // enumerated to keep labels unique
    // define Sets and Patterns for matching terminal elements
    private final Set<String> keywordSet = new HashSet<>(Arrays.asList("class",
            "constructor", "function", "method", "field", "static",
            "var", "int", "char", "boolean", "void", "true", "false",
            "if", "else", "while", "return"));
    private final Set<String> symbolSet = new HashSet<>(Arrays.asList("{", "}",
            "(", ")", "[", "]", ".", ",", ";", "+",
            "-", "*", "/", "&", "|", "<", ">", "=", "~"));
    private final Set<String> keyWordConstantSet = new HashSet<>(Arrays.asList("true", "false",
            "null", "this"));
    private final Set<String> binaryOpSet = new HashSet<>(Arrays.asList("+", "-",
            "*", "/", "&", "|", "<", ">", "="));
    private final Pattern intConstPattern = Pattern.compile("\\d+");
    private final Pattern stringConstPattern = Pattern.compile("\".+\"");
    private final Pattern identifierPattern = Pattern.compile("\\D[\\w_]+");

    private final String terminalTemplate = "<%1$s>%2$s</%1$s>\n";

    public Parser(File inputFile) throws IOException {
        this.tokenizer = new Tokenizer(inputFile);
        this.outputFilePath = extractFileNameWithoutExtension(inputFile.toString()) + ".xml";
        this.subroutineST = new SubroutineSymbolTable();
        this.classST = new ClassSymbolTable();
        this.codeGenerator = new CodeGenerator();
    }

    public SubroutineSymbolTable getSubroutineST() {
        return subroutineST;
    }

    public ClassSymbolTable getClassST() {
        return classST;
    }

    public void parse() throws IOException {
        File outputFile = new File(this.outputFilePath);
        this.subroutineST = new SubroutineSymbolTable();
        this.classST = new ClassSymbolTable();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(compileClass());
        }
    }

    /**
     * Compiles the XML representation of a class variable declaration
     * @return the XML representation as a string
     */
    public String compileClassVarDec() {
        StringBuilder sb = new StringBuilder();
        sb.append("<classVarDec>\n");
        sb.append(formatFromTemplate("keyword", tokenizer.getNextToken())); // "static" or "field"
        String varType = tokenizer.getNextToken(); // type
        String variable = tokenizer.getNextToken();
        sb.append(compileTypeAndVar(varType, variable));
        String nextToken = tokenizer.getNextToken();
        while (nextToken.equals(",")) {
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(formatFromTemplate("identifier", tokenizer.getNextToken()));
            nextToken = tokenizer.getNextToken();
        }
        sb.append(formatFromTemplate("symbol", nextToken));
        sb.append("</classVarDec>\n");
        return sb.toString();
    }

    /**
     * Setter method for testing purposes
     * @param className the class name used to set the current class name
     */
    public void setCurrentClassName(String className) {
        this.currentClassName = className;
    }
    /**
     * Compiles the XML representation of a parameter list, including the parentheses
     * @return the XML representation of a parameter list, including the parentheses
     */
    public String compileParamList() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // the opening (
        sb.append("<parameterList>\n");
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals(")")) { // )
            sb.append("</parameterList>\n");
            sb.append(formatFromTemplate("symbol", nextToken));
            return sb.toString();
        }
        // first variable declaration
        String varType = nextToken;
        String variable = tokenizer.getNextToken();
        sb.append(compileTypeAndVar(varType, variable));
        nextToken = tokenizer.getNextToken();
        while (nextToken.equals(",")) {
            sb.append(formatFromTemplate("symbol", nextToken)); // the comma
            varType = tokenizer.getNextToken();
            variable = tokenizer.getNextToken();
            sb.append(compileTypeAndVar(varType, variable));
            nextToken = tokenizer.getNextToken();
        }
        sb.append("</parameterList>\n");
        sb.append(formatFromTemplate("symbol", nextToken)); // closing )
        return sb.toString();
    }

    /**
     * Returns a formatted string using a template
     * @param terminalType the type of terminal element, as specified by the Jack grammar
     * @param element the element itself
     * @return a formatted string with the terminal element type and element substituted in
     */
    private String formatFromTemplate(String terminalType, String element) {
        if (symbolSet.contains(element)) {
            switch(element) {
                case "<":
                    element = "&lt;";
                    break;
                case ">":
                    element = "&gt;";
                    break;
                case "\"":
                    element = "&quot;";
                    break;
                case "&":
                    element = "&amp;";
                    break;
                default:
                    break;
            }
        }
        return String.format(terminalTemplate, terminalType, element);
    }

    /**
     * Compiles the XML representation of a general variable declaration (e.g. int myInt)
     * @param type the variable type
     * @param variable the name of the variable
     * @return the XML representation of a general variable declaration
     */
    private String compileTypeAndVar(String type, String variable) {
        StringBuilder sb = new StringBuilder();
        if (keywordSet.contains(type)) {
            sb.append(formatFromTemplate("keyword", type));
        } else {
            sb.append(formatFromTemplate("identifier", type));
        }
        sb.append(formatFromTemplate("identifier", variable));
        return sb.toString();
    }

    /**
     * Compiles an expression into VM code
     * @return an expression in VM code
     */
    public String compileExpression() {
        StringBuilder sb = new StringBuilder();
        sb.append(compileTerm());
        if (!tokenizer.hasNextToken()) {
            return sb.toString();
        }
        String nextToken = tokenizer.getNextToken();
        while (binaryOpSet.contains(nextToken)) {
            sb.append(compileTerm());
            sb.append(codeGenerator.generateArithLogical(nextToken));
            if (!tokenizer.hasNextToken()) {
                return sb.toString();
            }
            nextToken = tokenizer.getNextToken();
        }
        tokenizer.backTrack();
        return sb.toString();
    }


    /**
     * Compiles a Jack term into the corresponding VM code
     * @return VM code for a term
     */
    public String compileTerm() {
        StringBuilder sb = new StringBuilder();
        String nextToken = tokenizer.getNextToken();
        Matcher intConstantMatcher = intConstPattern.matcher(nextToken);
        Matcher strConstantMatcher = stringConstPattern.matcher(nextToken);
        if (intConstantMatcher.find()) { // integer constant
            sb.append(codeGenerator.generatePush(MemorySegment.CONSTANT, Integer.parseInt(nextToken)));
        } else if (strConstantMatcher.find()) { // string constant, need to strip off quotes
            sb.append(codeGenerator.generateStringLiteral(nextToken.replaceAll("\"", "")));
        } else if (keyWordConstantSet.contains(nextToken)) { // keyword constant
            sb.append(codeGenerator.generateKeywordConstant(nextToken));
        } else if (nextToken.equals("(")) { // (expression)
            sb.append(compileExpression());
            tokenizer.getNextToken(); // )
        } else if (symbolSet.contains(nextToken)) { // unaryOp
            sb.append(compileTerm());
            sb.append(codeGenerator.generateUnaryOp(nextToken));
        } else { // either just varName, array access or subroutine call
              /* Not sure why this block is here, seems unnecessary */
            if (!tokenizer.hasNextToken()) {
                Symbol unpackedSymbol = lookUpSymbol(nextToken);
                MemorySegment memSeg = getSymbolMemSeg(unpackedSymbol); // give memSeg an arbitrary starting value
                sb.append(codeGenerator.generatePush(memSeg, unpackedSymbol.getNumKind()));
                return sb.toString();
            }
            String nextNextToken = tokenizer.getNextToken();
            if (nextNextToken.equals("[")) { // array access
                Symbol unpackedSymbol = lookUpSymbol(nextToken);
                sb.append(codeGenerator.generatePush(getSymbolMemSeg(unpackedSymbol), unpackedSymbol.getNumKind()));
                sb.append(compileExpression());
                sb.append(codeGenerator.generateArithLogical("+"));
                sb.append(codeGenerator.generatePop(MemorySegment.POINTER, 1));
                sb.append(codeGenerator.generatePush(MemorySegment.THAT, 0));
                tokenizer.getNextToken(); // ]
            } else if (nextNextToken.equals("(") || nextNextToken.equals(".")) { // subroutine call
                for (int i = 0; i < 2; i++) {
                    tokenizer.backTrack(); // backtrack two spots to before the identifier
                }
                sb.append(compileSubroutineCall());
            } else { // just identifier
                Symbol unpackedSymbol = lookUpSymbol(nextToken);
                MemorySegment memSeg = getSymbolMemSeg(unpackedSymbol); // give memSeg an arbitrary starting value
                sb.append(codeGenerator.generatePush(memSeg, unpackedSymbol.getNumKind()));
                tokenizer.backTrack(); // spit out the nextNextToken
            }
        }
        return sb.toString();
    }

    /**
     * Compiles the VM code for a subroutine call
     * @return the VM code for a subroutine call
     */
    private String compileSubroutineCall() {
        StringBuilder sb = new StringBuilder();
        String className;
        String subroutineName;
        String nextToken = tokenizer.getNextToken(); // identifier
        String nextNextToken = tokenizer.getNextToken(); // either ( or .
        boolean isMethod = false;

        if (nextNextToken.equals(".")) { // not a method in the same class
            Symbol symbol;
            if (subroutineST.hasSymbol(nextToken) || classST.hasSymbol(nextToken)) {
                Optional<Symbol> option = subroutineST.lookUp(nextToken);
                if (!option.isPresent()) {
                    option = classST.lookUp(nextToken);
                }
                symbol = option.get();
                className = symbol.getDataType();
                sb.append(codeGenerator.generatePush(getSymbolMemSeg(symbol), symbol.getNumKind()));
                isMethod = true;
            } else { // function call
                className = nextToken;
            }
            subroutineName = tokenizer.getNextToken();
            tokenizer.getNextToken(); // (
        } else { // method in the same class
            className = this.currentClassName;
            subroutineName = nextToken;
            sb.append(codeGenerator.generatePush(MemorySegment.POINTER, 0));
            isMethod = true;
        }
        String fullSubroutineName = String.format("%s.%s", className, subroutineName);
        ExpressionList compiledExpList = compileExpressionList();
        int numArgs = compiledExpList.getNumExpressions();
        if (isMethod) {
            numArgs++;
        }
        sb.append(compiledExpList.getVmCode());
        sb.append(codeGenerator.generateFuncCall(fullSubroutineName, numArgs));
        tokenizer.getNextToken(); // )
        return sb.toString();
    }

    /**
     * Compiles VM code for a list of expressions
     * @return the VM code for a list of expressions
     */
    public ExpressionList compileExpressionList() {
        StringBuilder sb = new StringBuilder();
        int numExpressions = 0;
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals(")")) { // empty expression list
            tokenizer.backTrack();
            return new ExpressionList(sb.toString(), numExpressions);
        }
        tokenizer.backTrack();
        sb.append(compileExpression());
        numExpressions++;
        if (!tokenizer.hasNextToken()) {
            return new ExpressionList(sb.toString(), numExpressions);
        }
        nextToken = tokenizer.getNextToken();
        if (!nextToken.equals(",")) {
            tokenizer.backTrack();
            return new ExpressionList(sb.toString(), numExpressions);
        }
        while (nextToken.equals(",")) {
            sb.append(compileExpression());
            numExpressions++;
            if (!tokenizer.hasNextToken()) {
                return new ExpressionList(sb.toString(), numExpressions);
            }
            nextToken = tokenizer.getNextToken();
        }
        tokenizer.backTrack();
        return new ExpressionList(sb.toString(), numExpressions);
    }

    /**
     * Compiles the XML representation of a let statement
     * @return the XML representation of a let statement
     */
    public String compileLetStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatFromTemplate("identifier", tokenizer.getNextToken())); // varName
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals("[")) {
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(compileExpression());
            sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // ]
            nextToken = tokenizer.getNextToken();
        }
        sb.append(formatFromTemplate("symbol", nextToken)); // =
        sb.append(compileExpression());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // ;
        sb.append("</letStatement>\n");
        return sb.toString();
    }

    /**
     * Compiles the VM code for an if statement
     * @return the VM code for an if statement
     */
    public String compileIfStatement() {
        StringBuilder sb = new StringBuilder();
        tokenizer.getNextToken(); // (
        sb.append(compileExpression());
        sb.append(codeGenerator.generateUnaryOp("~")); // negate the expression
        sb.append(codeGenerator.generateIfGOTO("LBL_" + numLabels));
        int labelOne = numLabels; // save this
        numLabels++;
        tokenizer.getNextToken(); // )
        tokenizer.getNextToken(); // {
        sb.append(compileStatements());
        sb.append(codeGenerator.generateGOTO("LBL_" + numLabels));
        int labelTwo = numLabels; // save before incrementing
        numLabels++;
        tokenizer.getNextToken(); // }
        if (!tokenizer.hasNextToken()) {
            sb.append(codeGenerator.generateLabel("LBL_" + labelOne));
            sb.append(codeGenerator.generateLabel("LBL_" + labelTwo));
            return sb.toString();
        }
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals("else")) {
            tokenizer.getNextToken(); // {
            sb.append(codeGenerator.generateLabel("LBL_" + labelOne));
            sb.append(compileStatements());
            sb.append(codeGenerator.generateLabel("LBL_" + labelTwo));
            tokenizer.getNextToken(); // }
        } else {
            sb.append(codeGenerator.generateLabel("LBL_" + labelOne));
            sb.append(codeGenerator.generateLabel("LBL_" + labelTwo));
            tokenizer.backTrack();
        }
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a while statement
     * @return the XML representation of a while statement
     */
    public String compileWhileStatement() {
        StringBuilder sb = new StringBuilder();
        tokenizer.getNextToken(); // (
        // store the label numbers
        int labelOne = numLabels;
        numLabels++;
        int labelTwo = numLabels;
        numLabels++;
        sb.append(codeGenerator.generateLabel("LBL_" + labelOne));
        sb.append(compileExpression());
        sb.append(codeGenerator.generateUnaryOp("~")); // negate the expression
        tokenizer.getNextToken(); // )
        tokenizer.getNextToken(); // {
        sb.append(codeGenerator.generateIfGOTO("LBL_" + labelTwo));
        sb.append(compileStatements());
        sb.append(codeGenerator.generateGOTO("LBL_" + labelOne));
        tokenizer.getNextToken(); // }
        sb.append(codeGenerator.generateLabel("LBL_" + labelTwo));
        return sb.toString();
    }

    /**
     * Compiles the VM code for a do statement, does not include the "do" keyword
     * @return the VM code for a do statement, does not include the "do" keyword
     */
    public String compileDoStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append(compileSubroutineCall());
        tokenizer.getNextToken(); // ;
        sb.append(codeGenerator.generatePop(MemorySegment.TEMP, 0)); // pop off the useless value
        return sb.toString();
    }

    /**
     * Compiles the VM code for a return statement, does not include the "return" keyword
     * @return the VM code for a return statement, does not include the "return" keyword
     */
    public String compileReturnStatement() {
        StringBuilder sb = new StringBuilder();
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals(";")) {
            sb.append("return\n");
            return sb.toString();
        } else {
            tokenizer.backTrack();
            sb.append(compileExpression());
            tokenizer.getNextToken(); // ;
        }
        sb.append("return\n");
        return sb.toString();
    }


    /**
     * Compiles zero or more statements
     * @return the XML representation of statements(s)
     */
    public String compileStatements() {
        StringBuilder sb = new StringBuilder();
        String nextToken = tokenizer.getNextToken();
        Set<String> possibleStatements = new HashSet<>(Arrays.asList("let", "if",
                "while", "do", "return"));
        if (!possibleStatements.contains(nextToken)) {
            tokenizer.backTrack();
            return "";
        }
        // use reflection to call the appropriate statement compilation method
        String methodName = "compile" + nextToken.substring(0, 1).toUpperCase() + nextToken.substring(1) + "Statement";
        try {
            sb.append(this.getClass().getMethod(methodName).invoke(this));
            while (tokenizer.hasNextToken()) {
                nextToken = tokenizer.getNextToken();
                if (possibleStatements.contains(nextToken)) {
                    // use reflection to call the appropriate statement compilation method
                    methodName = "compile" + nextToken.substring(0, 1).toUpperCase() + nextToken.substring(1) + "Statement";
                    try {
                        sb.append(this.getClass().getMethod(methodName).invoke(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    tokenizer.backTrack();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Compiles the declaration of a local variable into XML
     * @return the XML representation of the declaration of a local variable
     */
    public String compileVarDec() {
        StringBuilder sb = new StringBuilder();
        sb.append("<varDec>\n");
        sb.append(formatFromTemplate("keyword", tokenizer.getNextToken())); // var
        String nextToken = tokenizer.getNextToken(); // type
        if (keywordSet.contains(nextToken)) {
            sb.append(formatFromTemplate("keyword", nextToken));
        } else {
            sb.append(formatFromTemplate("identifier", nextToken));
        }
        sb.append(formatFromTemplate("identifier", tokenizer.getNextToken()));
        nextToken = tokenizer.getNextToken();
        while (nextToken.equals(",")) {
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(formatFromTemplate("identifier", tokenizer.getNextToken()));
            nextToken = tokenizer.getNextToken();
        }
        sb.append(formatFromTemplate("symbol", nextToken));
        sb.append("</varDec>\n");
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a subroutine body declaration
     * @return the XML representation of a subroutine body declaration
     */
    public String compileSubroutineBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<subroutineBody>\n");
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken()));
        String nextToken = tokenizer.getNextToken();
        while (nextToken.equals("var")) {
            tokenizer.backTrack();
            sb.append(compileVarDec());
            nextToken = tokenizer.getNextToken();
        }
        tokenizer.backTrack();
        sb.append(compileStatements());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken()));
        sb.append("</subroutineBody>\n");
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a subroutine declaration
     * @return the XML representation of a subroutine declaration
     */
    public String compileSubroutineDec() {
        StringBuilder sb = new StringBuilder();
        sb.append("<subroutineDec>\n");
        // "constructor" or "function" or "method"
        sb.append(formatFromTemplate("keyword", tokenizer.getNextToken()));
        String nextToken = tokenizer.getNextToken(); // return type
        if (keywordSet.contains(nextToken)) {
            sb.append(formatFromTemplate("keyword", nextToken));
        } else {
            sb.append(formatFromTemplate("identifier", nextToken));
        }
        sb.append(formatFromTemplate("identifier", tokenizer.getNextToken())); // subroutine name
        sb.append(compileParamList());
        sb.append(compileSubroutineBody());
        sb.append("</subroutineDec>\n");
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a class declaration
     * @return the XML representation of a class declaration
     */
    public String compileClass() {
        StringBuilder sb = new StringBuilder();
//        sb.append("<class>\n");
//        sb.append(formatFromTemplate("keyword", tokenizer.getNextToken())); // "class"
//        sb.append(formatFromTemplate("identifier", tokenizer.getNextToken())); // currentClassName
        tokenizer.getNextToken(); // the "class" keyword
        this.currentClassName = tokenizer.getNextToken(); // name of the class
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // {
        String nextToken = tokenizer.getNextToken();
        while (nextToken.equals("static") || nextToken.equals("field")) {
            tokenizer.backTrack();
            sb.append(compileClassVarDec());
            nextToken = tokenizer.getNextToken();
        }
        Set<String> subroutineDecKeywords = new HashSet<>(Arrays.asList("constructor", "function", "method"));
        while (subroutineDecKeywords.contains(nextToken)) {
            tokenizer.backTrack();
            sb.append(compileSubroutineDec());
            nextToken = tokenizer.getNextToken();
        }
        sb.append(formatFromTemplate("symbol", nextToken)); // }
        sb.append("</class>\n");
        return sb.toString();
    }

    /**
     * Extracts the file path of a file without the extension
     * @param filePath the file path
     * @return the file path of a file without the extension
     */
    private String extractFileNameWithoutExtension(String filePath) {
        int indexOfPeriod = filePath.lastIndexOf(".");
        return filePath.substring(0, indexOfPeriod);
    }

    /**
     * Retrives the corresponding MemorySegment of a Symbol
     * @param symbol a Symbol object
     * @return the corresponding MemorySegment of a Symbol
     */
    private MemorySegment getSymbolMemSeg(Symbol symbol) {
        MemorySegment memSeg = MemorySegment.LOCAL; // give memSeg an arbitrary starting value
        switch (symbol.getSymbolKind()) {
            case FIELD:
                memSeg = MemorySegment.THIS;
                break;
            case STATIC:
                memSeg = MemorySegment.STATIC;
                break;
            case ARGUMENT:
                memSeg = MemorySegment.ARGUMENT;
                break;
            case LOCAL:
                memSeg = MemorySegment.LOCAL;
                break;
        }
        return memSeg;
    }

    /**
     * Looks up a symbol from the two symbol tables, assuming that the symbol exists
     * @param symbolName the name of the symbol
     * @return the found Symbol object
     */
    private Symbol lookUpSymbol(String symbolName) {
        Optional<Symbol> symbol = subroutineST.lookUp(symbolName);
        if (!symbol.isPresent()) {
            symbol = classST.lookUp(symbolName);
        }
        /*
         * probably not needed, can assume Jack programs are well formed and will not
         * use a variable that is undeclared
         */
        if (!symbol.isPresent()) {
            System.out.println(String.format("Symbol %s cannot be found", symbolName));
            throw new RuntimeException();
        }
        return symbol.get();
    }
}
