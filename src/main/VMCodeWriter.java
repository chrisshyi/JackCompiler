package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class responsible for writing VM code into an output file
 */
public class VMCodeWriter {

    private BufferedWriter writer;

    public VMCodeWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    /**
     * Writes a VM push instruction to the output file
     * @param segment the memory segment to push from
     * @param index the index of the memory segment to push from
     * @throws IOException IOException
     */
    public void writePush(MemorySegment segment, int index) throws IOException {
        String vmCode = String.format("push %s %d\n", segment.name().toLowerCase(), index);
        writer.write(vmCode);
    }

    /**
     * Writes a VM pop instruction to the output file
     * @param segment the memory segment to pop to
     * @param index the index of the memory segment to pop to
     * @throws IOException IOException
     */
    public void writePop(MemorySegment segment, int index) throws IOException {
        String vmCode = String.format("pop %s %d\n", segment.name().toLowerCase(), index);
        writer.write(vmCode);
    }

    /**
     * Writes an arithmetic/logical VM instruction to the output file
     * @param command the command
     * @throws IOException IOException
     */
    public void writeArithLogical(String command) throws IOException {
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
        String vmCode = String.format("%s\n", toWrite);
        writer.write(vmCode);
    }
}
