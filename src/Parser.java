import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Works in tandem with a Tokenizer object to generate a parsetree
 */
public class Parser {

    private Tokenizer tokenizer;
    // define Sets and Patterns for matching terminal elements
    private final Set<String> keywordSet = new HashSet<>(Arrays.asList("class",
            "constructor", "function", "method", "field", "static",
            "var", "int", "char", "boolean", "void", "true", "false",
            "if", "else", "while", "return"));
    private final Set<String> symbolSet = new HashSet<>(Arrays.asList("{", "}",
            "(", ")", "[", "]", ".", ",", ";", "+",
            "-", "*", "/", "&", "|", "<", ">", "=", "~"));
    private final Pattern intConstPattern = Pattern.compile("\\d+");
    private final Pattern stringConstPattern = Pattern.compile("\"[\\w ]+\"");
    private final Pattern identifierPattern = Pattern.compile("\\D[\\w_]+");

    private final String terminalTemplate = "<%1$s>%2$s</%1$s>\n";

    public Parser(File inputFile) throws IOException {
        this.tokenizer = new Tokenizer(inputFile);
    }

    public void parse(File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

        }
    }

    /**
     * Recursively compiles the XML representation of a class declaration
     * @return
     */
    String compileClass() {
        return null;
    }

    /**
     * Compiles the XML representation of a class variable declaration
     * @return the XML representation as a string
     */
    String compileClassVarDec() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatFromTemplate("keyword", tokenizer.getNextToken()));
        String varType = tokenizer.getNextToken();
        String variable = tokenizer.getNextToken();
        sb.append(compileTypeAndVar(varType, variable));
        String nextToken = tokenizer.getNextToken();
        while (nextToken.equals(",")) {
            sb.append(formatFromTemplate("symbol", nextToken));
            sb.append(formatFromTemplate("identifier", tokenizer.getNextToken()));
            nextToken = tokenizer.getNextToken();
        }
        sb.append(formatFromTemplate("symbol", nextToken));
        return sb.toString();
    }

    /**
     * Compiles the XML representation of a parameter list, including the parentheses
     * @return the XML representation of a parameter list, including the parentheses
     */
    String compileParamList() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatFromTemplate("symbol", tokenizer.getNextToken())); // the opening (
        // first variable declaration
        String varType = tokenizer.getNextToken();
        String variable = tokenizer.getNextToken();
        sb.append(compileTypeAndVar(varType, variable));
        String nextToken = tokenizer.getNextToken();
        while (nextToken.equals(",")) {
            sb.append(formatFromTemplate("symbol", nextToken)); // the comma
            varType = tokenizer.getNextToken();
            variable = tokenizer.getNextToken();
            sb.append(compileTypeAndVar(varType, variable));
            nextToken = tokenizer.getNextToken();
        }
        sb.append(formatFromTemplate("symbol", nextToken)); // closing )
        return sb.toString();
    }

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
}
