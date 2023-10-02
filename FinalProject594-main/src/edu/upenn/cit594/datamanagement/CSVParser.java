package edu.upenn.cit594.datamanagement;

import java.util.ArrayList;
import java.util.List;

//package edu.upenn.cit594.datamanagement;
public class CSVParser {
    private static final char DELIMITER = ',';

    /**
     * Takes in a string and returns an array of data (tokens) based on comma delimiter
     *
     * @param line to read
     * @return array of tokens
     */
    static String[] parseLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == DELIMITER && !inQuotes) {
                tokens.add(sb.toString().trim().replaceAll("^\"|\"$", ""));
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim().replaceAll("^\"|\"$", ""));
        return tokens.toArray(new String[0]);
    }
}
