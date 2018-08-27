package main;

import java.io.File;
import java.io.IOException;

public class JackCompiler {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java VMTranslator inputFile");
            return;
        }
        // note that the "file" in inputFilePath is used interchangeably with directory
        String inputFilePath = args[0];
        // if the input file is a directory and its path ends with the separator,
        // truncate it
        if (inputFilePath.charAt(inputFilePath.length() - 1) == File.separatorChar) {
            inputFilePath = inputFilePath.substring(0, inputFilePath.length() - 1);
        }
        File inputFile = new File(inputFilePath);
        File[] filesToTranslate;
        if (inputFile.isDirectory()) {
            filesToTranslate = inputFile.listFiles();
        } else {
            filesToTranslate = new File[1];
            filesToTranslate[0] = inputFile;
        }
        for (File jackFile : filesToTranslate) {
            if (jackFile.toString().endsWith(".jack")) {
                Parser parser = new Parser(jackFile);
                parser.parse();
            }
        }
    }
}
