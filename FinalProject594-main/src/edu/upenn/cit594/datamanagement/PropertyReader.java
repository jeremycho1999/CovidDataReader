package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PropertyData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PropertyReader {

    List<PropertyData> propertyDataList = new ArrayList<>();
    private static final String ZIP_CODE_FIELD = "zip_code";
    private static final String MARKET_VALUE_FIELD = "market_value";
    private static final String TOTAL_LIVABLE_AREA_FIELD = "total_livable_area";
    private Map<String, Integer> headerMap = new HashMap<>();
    private List<String> fieldList = new ArrayList<>(Arrays.asList(ZIP_CODE_FIELD,MARKET_VALUE_FIELD,TOTAL_LIVABLE_AREA_FIELD));
    String inputFileName;

    public PropertyReader(String propertyFileName) {
        this.inputFileName = propertyFileName;
    }

    /**
     * Reads csv file of property data and assigns the data to instance propertyDataList
     *
     * @return list of property data
     */
    public List<PropertyData> getPropertyData() throws Exception {
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
                    double market_value = -1;
                    double total_livable_area = -1;
                    if (tokens[headerMap.get(ZIP_CODE_FIELD)].length() >= 5) {
                        try {
                            zip = Integer.parseInt(tokens[headerMap.get(ZIP_CODE_FIELD)].substring(0,5));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    try {
                        market_value = Double.parseDouble(tokens[headerMap.get(MARKET_VALUE_FIELD)]);
                    } catch (Exception e) {
                        market_value = Double.NEGATIVE_INFINITY;
                    }
                    try {
                        total_livable_area = Double.parseDouble(tokens[headerMap.get(TOTAL_LIVABLE_AREA_FIELD)]);
                    } catch (Exception e) {
                        total_livable_area = Double.NEGATIVE_INFINITY;
                    }
                    if (zip != -1 && market_value != -1 && total_livable_area != -1) {
                        PropertyData data = new PropertyData(zip, market_value, total_livable_area);
                        propertyDataList.add(data);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return propertyDataList;
    }
}
