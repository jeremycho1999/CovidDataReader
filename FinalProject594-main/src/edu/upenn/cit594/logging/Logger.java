package edu.upenn.cit594.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class Logger {
    private static String inputFileName;
    private static final Logger logger= new Logger();

    private Logger(){

    }
    public static Logger getInstance() {
        return logger;
    }
    public static void setDestination(String inputFileName) {
        Logger.inputFileName = inputFileName;
    }

    /**
     * Writes the given string to instance variable inputFileName
     *
     * @param strToWrite
     */
    public void writeToLog(String strToWrite) {
        long currentTime = System.currentTimeMillis();
        if (inputFileName != null && !inputFileName.isEmpty()) {
            if (strToWrite == null) {
                System.out.println("String to write cannot be null.");
            } else {
                try {
                    FileWriter fw = new FileWriter(inputFileName, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(currentTime + " " + strToWrite);
                    bw.newLine();
                    bw.close();
                } catch (IOException var4) {
                    System.out.println("Error writing to file");
                }
            }
        } else {
            System.out.println("File name cannot be null or empty.");
        }
    }

    /**
     * Writes cmd line args to the inputFileName
     *
     * @param args to write
     */
    public void writeCMDLineArgs(String[] args) {
        if (args.length > 0) {
            writeToLog(Arrays.toString(args));
        }
    }

    /**
     * Writes files to read to the inputFileName
     *
     * @param fileName to write
     */
    public void writeFilesToRead(String fileName) {
        if (fileName != null) {
            writeToLog(fileName);
        }
    }

    /**
     * Writes user responses to inputFileName
     *
     * @param response
     */
    public void writeUserResponses(String response) {
        if (response != null) {
            writeToLog(response);
        }
    }

}