import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Works in tandem with a Tokenizer object to generate a parsetree
 */
public class Parser {

    private Tokenizer tokenizer;
    private String outputFilePath;
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
    private final Pattern stringConstPattern = Pattern.compile("\"[\\w ]+\"");
    private final Pattern identifierPattern = Pattern.compile("\\D[\\w_]+");

    private final String terminalTemplate = "<%1$s>%2$s</%1$s>\n";

    public Parser(File inputFile) throws IOException {
        this.tokenizer = new Tokenizer(inputFile);
        this.outputFilePath = extractFileNameWithoutExtension(inputFile.toString()) + ".xml";
    }

    public void parse() throws IOException {
        File outputFile = new File(this.outputFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(compileClass());
        }
    }

    /**
     * Compiles the XML representation of a class variable declaration
     * @return the XML representation as a string
     */
    String compileClassVarDec() {
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
     * Compiles the XML representation of a parameter list, including the parentheses
     * @return the XML representation of a parameter list, including the parentheses
     */
    String compileParamList() {
        StringBuilder sb = new StringBuilder();
        sb.append("<parameterList>\n");
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // the opening (
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals(")")) { // )
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append("</parameterList>\n");
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
        sb.append(formatFromTemplate("symbol", nextToken)); // closing )
        sb.append("</parameterList>\n");
        return sb.toString();
    }

    /**
     * Returns a formatted string using a template
     * @param terminalType the type of terminal element, as specified by the Jack grammar
     * @param element the element itself
     * @return a formatted string with the terminal element type and element substituted in
     */
    private String formatFromTemplate(String terminalType, String element) {
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
     * Compiles an expression
     * @return
     */
    public String compileExpression() {
        StringBuilder sb = new StringBuilder();
        sb.append(compileTerm());
        if (!tokenizer.hasNextToken()) {
            return sb.toString();
        }
        String nextToken = tokenizer.getNextToken();
        if (binaryOpSet.contains(nextToken)) {
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(compileTerm());
        } else {
            tokenizer.backTrack();
        }
        return sb.toString();
    }


    /**
     * Compiles the XML representation of a term, recursively
     * @return the XML representation of a term, as dictated by the Jack grammar
     */
    public String compileTerm() {
        StringBuilder sb = new StringBuilder();
        String nextToken = tokenizer.getNextToken();
        Matcher intConstantMatcher = intConstPattern.matcher(nextToken);
        Matcher strConstantMatcher = stringConstPattern.matcher(nextToken);
        if (intConstantMatcher.find()) { // integer constant
            sb.append(formatFromTemplate("integerConstant", nextToken));
        } else if (strConstantMatcher.find()) { // string constant, need to strip off quotes
            sb.append(formatFromTemplate("StringConstant", nextToken.replaceAll("\"", "")));
        } else if (keyWordConstantSet.contains(nextToken)) { // keyword constant
            sb.append(formatFromTemplate("keyword", nextToken));
        } else if (nextToken.equals("(")) { // (expression)
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(compileExpression());
            sb.append(formatFromTemplate("symbol", tokenizer.getNextToken()));
        } else if (symbolSet.contains(nextToken)) { //unaryOp
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(compileTerm());
        } else { // either just varName, array indexing or subroutine call
            if (!tokenizer.hasNextToken()) {
                sb.append(formatFromTemplate("identifier", nextToken));
                return sb.toString();
            }
            String nextNextToken = tokenizer.getNextToken();
            if (nextNextToken.equals("[")) {
                sb.append(formatFromTemplate("identifier", nextToken));
                sb.append(formatFromTemplate("symbol", nextNextToken)); // [
                sb.append(compileExpression());
                sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // ]
            } else if (nextNextToken.equals("(") || nextNextToken.equals(".")) { // subroutine call
                for (int i = 0; i < 2; i++) {
                    tokenizer.backTrack(); // backtrack two spots to before the identifier
                }
                sb.append(compileSubroutineCall());
            } else {
                sb.append(formatFromTemplate("identifier", nextToken));
                tokenizer.backTrack();
            }
        }
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a subroutine call
     * @return the XML representation of a subroutine call
     */
    private String compileSubroutineCall() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatFromTemplate("identifier", tokenizer.getNextToken()));
        String nextToken = tokenizer.getNextToken();
        sb.append(formatFromTemplate("symbol", nextToken)); // either ( or .
        if (nextToken.equals(".")) {
            sb.append(formatFromTemplate("identifier", tokenizer.getNextToken())); // subroutineName
            sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // (
        }
        sb.append(compileExpressionList());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // )
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a list of expressions
     * @return the XML representation of a list of expressions
     */
    public String compileExpressionList() {
        StringBuilder sb = new StringBuilder();
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals(")")) { // empty expression list
            tokenizer.backTrack();
            return "";
        }
        tokenizer.backTrack();
        sb.append(compileExpression());
        if (!tokenizer.hasNextToken()) {
            return sb.toString();
        }
        nextToken = tokenizer.getNextToken();
        if (!nextToken.equals(",")) {
            tokenizer.backTrack();
            return sb.toString();
        }
        while (nextToken.equals(",")) {
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(compileExpression());
            if (!tokenizer.hasNextToken()) {
                return sb.toString();
            }
            nextToken = tokenizer.getNextToken();
        }
        tokenizer.backTrack();
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a let statement
     * @return the XML representation of a let statement
     */
    public String compileLetStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatFromTemplate("identifier", tokenizer.getNextToken()));
        String nextToken = tokenizer.getNextToken();
        sb.append(formatFromTemplate("symbol", nextToken));
        if (nextToken.equals("[")) {
            sb.append(compileExpression());
            sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // ]
        }
        sb.append(compileExpression());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // ;
        return sb.toString();
    }

    /**
     * Compiles the XML representation of an if statement
     * @return the XML representation of an if statement
     */
    public String compileIfStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // (
        sb.append(compileExpression());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // )
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // {
        sb.append(compileStatements());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // }
        if (!tokenizer.hasNextToken()) {
            return sb.toString();
        }
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals("else")) {
            sb.append(formatFromTemplate("keyword", nextToken));
            sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // {
            sb.append(compileStatements());
            sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // }
        } else {
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
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // (
        sb.append(compileExpression());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // )
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // {
        sb.append(compileStatements());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // }
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a do statement
     * @return the XML representation of a do statement
     */
    public String compileDoStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append(compileSubroutineCall());
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // ;
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a return statement
     * @return the XML representation of a return statement
     */
    public String compileReturnStatement() {
        StringBuilder sb = new StringBuilder();
        String nextToken = tokenizer.getNextToken();
        if (nextToken.equals(";")) {
            sb.append(formatFromTemplate("symbol", nextToken));
        } else {
            tokenizer.backTrack();
            sb.append(compileExpression());
            sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // ;
        }
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
        sb.append(formatFromTemplate("keyword", nextToken));
        // use reflection to call the appropriate statement compilation method
        String methodName = "compile" + nextToken.substring(0, 1).toUpperCase() + nextToken.substring(1) + "Statement";
        try {
            sb.append(this.getClass().getMethod(methodName).invoke(this));
            while (tokenizer.hasNextToken()) {
                nextToken = tokenizer.getNextToken();
                if (possibleStatements.contains(nextToken)) {
                    tokenizer.backTrack();
                    sb.append(compileStatements());
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
    String compileVarDec() {
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a subroutine body declaration
     * @return the XML representation of a subroutine body declaration
     */
    String compileSubroutineBody() {
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
    String compileSubroutineDec() {
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
    String compileClass() {
        StringBuilder sb = new StringBuilder();
        sb.append("<class>\n");
        sb.append(formatFromTemplate("keyword", tokenizer.getNextToken())); // "class"
        sb.append(formatFromTemplate("identifier", tokenizer.getNextToken())); // className
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
}
