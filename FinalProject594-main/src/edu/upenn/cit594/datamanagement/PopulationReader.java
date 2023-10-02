package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PopulationReader {
    private static final String ZIP_CODE_FIELD = "zip_code";
    private static final String POPULATION_FIELD = "population";
    HashMap<Integer, Integer> populationDataHashMap = new HashMap<>();
    private Map<String, Integer> headerMap = new HashMap<>();
    private List<String> fieldList = new ArrayList<>(Arrays.asList(ZIP_CODE_FIELD,POPULATION_FIELD));
    private String inputFileName;

    /**
     * Reads csv file of population data and assigns the data to instance populationDataHashMap
     *
     * @return HashMap of population data
     */
    public PopulationReader(String populationFileName) {
        this.inputFileName = populationFileName;
    }

    public HashMap<Integer, Integer> getPopulationData() {
        boolean firstLine = true;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            Logger.getInstance().writeFilesToRead(inputFileName);
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = CSVParser.parseLine(line);
                if (firstLine) {
                    for (int i = 0; i < tokens.length; i++) {
                        if (fieldList.contains(tokens[i])) {
                            headerMap.put(tokens[i],i);
                        }
                    }
                    firstLine = false;
                } else {
                    int zip = -1;
                    int population = -1;
                    if (tokens[headerMap.get(ZIP_CODE_FIELD)].length() == 5) {
                        zip = Integer.parseInt(tokens[headerMap.get(ZIP_CODE_FIELD)]);
                    } else {
                        continue;
                    }
                    try {
                        population = Integer.parseInt(tokens[headerMap.get(POPULATION_FIELD)]);
                    } catch (Exception e) {
                        continue;
                    }
                    if (zip != -1 && population != -1) {
                        populationDataHashMap.put(zip, population);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return populationDataHashMap;
    }
}
