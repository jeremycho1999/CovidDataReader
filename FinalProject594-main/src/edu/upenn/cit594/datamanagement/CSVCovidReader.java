package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVCovidReader extends CovidReader {

    //set up all magic numbers here
    private static final String ZIP_CODE_FIELD = "zip_code";
    private static final String PARTIALLY_VACCINATED_FIELD = "partially_vaccinated";
    private static final String FULLY_VACCINATED_FIELD = "fully_vaccinated";
    private static final String ETL_TIMESTAMP_FIELD = "etl_timestamp";
    private static final String HOSPITALIZED_FIELD = "hospitalized";
    private List<String> fieldList = new ArrayList<>(Arrays.asList(ZIP_CODE_FIELD,PARTIALLY_VACCINATED_FIELD,FULLY_VACCINATED_FIELD,ETL_TIMESTAMP_FIELD,HOSPITALIZED_FIELD));
    private Map<String, Integer> headerMap = new HashMap<>();
    HashMap<Integer, HashMap<Date, CovidData>> covidDataHashMap = new HashMap<>();


    public CSVCovidReader(String fileName) {
        super(fileName);
    }

    /**
     * Reads csv file of covid data and assigns the data to instance covidDataHashMap
     *
     * @return HashMap of covid data
     */
    @Override
    public HashMap<Integer, HashMap<Date, CovidData>> getCovidData() {

        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            Logger.getInstance().writeFilesToRead(inputFileName);
            String line;
            boolean firstLine = true;
            int zip = -1;
            Date date = null;
            double partiallyVaccinated = -1;
            double fullyVaccinated = -1;
            double hospitalized = -1;
            while ((line = br.readLine()) != null) {
                String[] tokens = CSVParser.parseLine(line);
                if (firstLine) {
                    for (int i = 0; i < tokens.length; i++) {
                        if (fieldList.contains(tokens[i])) {
                            headerMap.put(tokens[i], i);
                        }
                    }
                    firstLine = false;
                } else {
                    if (tokens[headerMap.get(ZIP_CODE_FIELD)].length() == 5) {
                        zip = Integer.parseInt(tokens[headerMap.get(ZIP_CODE_FIELD)]);
                    } else {
                        continue;
                    }
                    try {
                        String stringYouGotFromCovidFile = tokens[headerMap.get(ETL_TIMESTAMP_FIELD)];
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
                        Date convertedDate = simpleDateFormat.parse(stringYouGotFromCovidFile);
                        date = convertedDate;
                    } catch (Exception e) {
                        continue;
                    }
                    try {
                        partiallyVaccinated = Integer.parseInt(tokens[headerMap.get(PARTIALLY_VACCINATED_FIELD)]);
                    } catch (Exception e) {
                        partiallyVaccinated = 0.0;
                    }
                    try {
                        fullyVaccinated = Integer.parseInt(tokens[headerMap.get(FULLY_VACCINATED_FIELD)]);
                    } catch (Exception e) {
                        fullyVaccinated = 0.0;
                    }
                    try {
                        hospitalized = Integer.parseInt(tokens[headerMap.get(HOSPITALIZED_FIELD)]);
                    } catch (Exception e) {
                        hospitalized = 0.0;
                    }
                    if (zip != -1 && date != null && partiallyVaccinated != -1 && fullyVaccinated != -1 && hospitalized != -1) {
                        CovidData data = new CovidData(date, partiallyVaccinated, fullyVaccinated,hospitalized);
                        if (covidDataHashMap.containsKey(zip)) {
                            covidDataHashMap.get(zip).put(date, data);
                        } else {
                            HashMap<Date, CovidData> CovidDataHashMap = new HashMap<>();
                            CovidDataHashMap.put(date, data);
                            covidDataHashMap.put(zip, CovidDataHashMap);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return covidDataHashMap;
    }
}
