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
    private Set<String> keywordSet = new HashSet<>(Arrays.asList("class",
            "constructor", "function", "method", "field", "static",
            "var", "int", "char", "boolean", "void", "true", "false",
            "if", "else", "while", "return"));
    private Set<String> symbolSet = new HashSet<>(Arrays.asList("{", "}",
            "(", ")", "[", "]", ".", ",", ";", "+",
            "-", "*", "/", "&", "|", "<", ">", "=", "~"));
    private Pattern intConstPattern = Pattern.compile("\\d+");
    private Pattern stringConstPattern = Pattern.compile("\"[\\w ]+\"");
    private Pattern identifierPattern = Pattern.compile("\\D[\\w_]+");

    public Parser(File inputFile) throws IOException {
        this.tokenizer = new Tokenizer(inputFile);
    }

    public void parse(File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            
        }
    }
}
