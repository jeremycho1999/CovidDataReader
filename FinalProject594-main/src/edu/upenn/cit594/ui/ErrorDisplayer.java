package edu.upenn.cit594.ui;

import java.nio.file.NoSuchFileException;

public class ErrorDisplayer {

    public static void displayInputSyntaxError(String message) {

        throw new IllegalArgumentException(message);
    }


    public static void displayFileExistingError(String argumentName) throws NoSuchFileException {

        throw new NoSuchFileException(String.format("File %s does not exist.",argumentName));
    }


    public static void displayFileOpeningError(String argumentName) throws NoSuchFileException {

        throw new NoSuchFileException(String.format("File %s cannot be opening for reading.",argumentName));
    }

}
