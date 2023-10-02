package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import edu.upenn.cit594.util.CovidData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSONCovidReader extends CovidReader{
    private static final String ZIP_CODE_FIELD = "zip_code";
    private static final String PARTIALLY_VACCINATED_FIELD = "partially_vaccinated";
    private static final String FULLY_VACCINATED_FIELD = "fully_vaccinated";
    private static final String ETL_TIMESTAMP_FIELD = "etl_timestamp";
    private static final String HOSPITALIZED_FIELD = "hospitalized";
    private HashMap<Integer,HashMap<Date, CovidData>> covidDataHashMap= new HashMap<>();

    public JSONCovidReader(String fileName) throws java.text.ParseException {
        super(fileName);
    }

    /**
     * Reads json file of covid data and assigns the data to instance covidDataHashMap
     *
     * @return HashMap of covid data
     */
    @Override
    public HashMap<Integer, HashMap<Date, CovidData>> getCovidData() throws IOException, java.text.ParseException {

        JSONParser parser = new JSONParser();
        JSONArray covidDataList = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(this.inputFileName);
            Logger.getInstance().writeFilesToRead(inputFileName);
            covidDataList = (JSONArray) parser.parse(fileReader);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Input file not found");
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
        for (Object o : covidDataList) {
            JSONObject data = (JSONObject) o;
            Object zip = data.get(ZIP_CODE_FIELD);
            if (zip.toString().length() != 5) {
                continue;
            }
            Object timestamp = data.get(ETL_TIMESTAMP_FIELD);
            Date convertedDate;
            try {
                String stringYouGotFromCovidFile = timestamp.toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                convertedDate = simpleDateFormat.parse(stringYouGotFromCovidFile);
            } catch (Exception e) {
                continue;
            }
            Object partiallyVaccinated = data.get(PARTIALLY_VACCINATED_FIELD);
            Object fullyVaccinated = data.get(FULLY_VACCINATED_FIELD);
            Object hospitalized = data.get(HOSPITALIZED_FIELD);
            if (partiallyVaccinated == null) {
                partiallyVaccinated = 0.0;
            }
            if (fullyVaccinated == null) {
                fullyVaccinated = 0.0;
            }
            if (hospitalized == null) {
                hospitalized = 0.0;
            }
            CovidData newData = new CovidData(convertedDate,
                    Double.parseDouble(String.valueOf(partiallyVaccinated)), Double.parseDouble(String.valueOf(fullyVaccinated)), Double.parseDouble(String.valueOf(hospitalized)));
            int zipKey = Integer.parseInt(zip.toString());
            if (covidDataHashMap.containsKey(zipKey)) {
                covidDataHashMap.get(zipKey).put(convertedDate, newData);
            } else {
                HashMap<Date, CovidData> CovidDataHashMap = new HashMap<>();
                CovidDataHashMap.put(convertedDate, newData);
                covidDataHashMap.put(zipKey, CovidDataHashMap);
            }
        }
        return covidDataHashMap;
    }

    /*
     //ADD THIS PIECE OF CODE FOR ALL DATES

    String stringYouGotFromCovidFile="2021-03-25";
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
    Date convertedDate= simpleDateFormat.parse(stringYouGotFromCovidFile);

    //Later, for UI, we'll need to revert it

    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
    String date= simpleDateFormat.format(date);

     */
}
