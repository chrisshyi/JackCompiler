import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    // TODO: which class should be responsible for closing the source file?
    private int currentIndex; // keeps track of where the Tokenizer is at in the current line
    private List<String> tokens;

    /**
     * Tokenizes a Jack source code file and initializes the Tokenizer
     * @param sourceFile the Jack source code file
     * @throws IOException IOException
     */
    public Tokenizer(File sourceFile) throws IOException {
        this.tokens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            Pattern tokenPattern = Pattern.compile("\"[\\w ?]+\"|[\\w_]+|\\d+|[\\{\\}\\(\\)\\[\\]\\.,;\\+\\-*/&|<>=~]");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = tokenPattern.matcher(line);
                while (matcher.find()) {
                    String match = line.substring(matcher.start(), matcher.end());
                    if (line.charAt(matcher.start()) == '/' && matcher.start() != line.length() - 1) {
                        if (line.substring(matcher.start(), matcher.start() + 2).equals("//")) {
                            break;
                        }
                    }
                    tokens.add(match);
                }
            }
        }
        this.currentIndex = 0;
    }

    public boolean hasNextToken() {
        return !(this.currentIndex == this.tokens.size());
    }

    /**
     * Returns the next token
     * @return the next token
     */
    public String getNextToken() {
        String currentToken = this.tokens.get(this.currentIndex);
        currentIndex++;
        return currentToken;
    }

    /**
     * Backtracks the token iteration, needed where Jack is an LL(2) language
     */
    public void backTrack() {
        currentIndex--;
    }
}
