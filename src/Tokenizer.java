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
            Pattern tokenPattern = Pattern.compile("\".+\"|[\\w_]+|\\d+|[\\{\\}\\(\\)\\[\\]\\.,;\\+\\-*/&|<>=~]");
            Pattern blockCommentStart = Pattern.compile("/\\*|/\\*\\*");
            Pattern blockCommentEnd = Pattern.compile("\\*/");
            Pattern inlineCommentPattern = Pattern.compile("//");
            int blockCommentStartIndex;
            int blockCommentEndIndex;
            int inlineCommentStartIndex;
            boolean activeBlockComment = false;
            while ((line = reader.readLine()) != null) {
                Matcher tokenMatcher = tokenPattern.matcher(line);
                Matcher inlineCommentMatcher = inlineCommentPattern.matcher(line);
                if (inlineCommentMatcher.find()) {
                    inlineCommentStartIndex = inlineCommentMatcher.start();
                } else {
                    inlineCommentStartIndex = line.length();
                }
                if (activeBlockComment) { // have met the start of a block comment but haven't seen the end
                    Matcher blockEndMatcher = blockCommentEnd.matcher(line);
                    if (blockEndMatcher.find()) {
                        blockCommentEndIndex = blockEndMatcher.end();
                        activeBlockComment = false;
                    } else {
                        blockCommentEndIndex = line.length();
                    }
                    while (tokenMatcher.find()) {
                        if (tokenMatcher.start() >= blockCommentEndIndex && tokenMatcher.start() < inlineCommentStartIndex) {
                            tokens.add(line.substring(tokenMatcher.start(), tokenMatcher.end()));
                        }
                    }
                } else {
                    Matcher blockStartMatcher = blockCommentStart.matcher(line);
                    Matcher blockEndMatcher = blockCommentEnd.matcher(line);
                    if (blockStartMatcher.find()) {
                        blockCommentStartIndex = blockStartMatcher.start();
                        activeBlockComment = true;
                        if (blockEndMatcher.find()) {
                            blockCommentEndIndex = blockEndMatcher.end();
                            activeBlockComment = false;
                        } else {
                            blockCommentEndIndex = line.length();
                        }
                    } else {
                        blockCommentStartIndex = line.length();
                        blockCommentEndIndex = 0;
                    }
                    while (tokenMatcher.find()) {
                        if (tokenMatcher.start() < blockCommentStartIndex && tokenMatcher.start() < inlineCommentStartIndex
                        && tokenMatcher.start() >= blockCommentEndIndex) {
                            tokens.add(line.substring(tokenMatcher.start(), tokenMatcher.end()));
                        }
                    }
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
