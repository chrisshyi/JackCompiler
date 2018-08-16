import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Tokenizer {

    // TODO: which class should be responsible for closing the source file?
    private File sourceFile;
    private BufferedReader reader;
    private String nextToken;
    private String[] currentLine;
    private int currentIndex; // keeps track of where the Tokenizer is at in the current line

    public Tokenizer(File sourceFile) throws IOException {
        this.sourceFile = sourceFile;
        this.reader = new BufferedReader(new FileReader(sourceFile));
        this.nextToken = "";
    }

    public boolean hasNextToken() throws IOException {
        // what about lines that start with '//'
        if (currentLine == null || (this.currentIndex == this.currentLine.length)
            || (this.currentLine[this.currentIndex].startsWith("//"))) {
            String line = reader.readLine();
            if (line == null) {
                return false;
            }
            while (line.startsWith("//")) {
                line = reader.readLine();
                if (line == null) {
                    return false;
                }
            }
            this.currentLine = line.split("\\s");
            this.currentIndex = 0;
        }
        return true;
    }

    public String getNextToken() {
        this.nextToken = this.currentLine[this.currentIndex];
        this.currentIndex++;
        return this.nextToken;
    }



}
