package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;

import java.util.Date;
import java.util.HashMap;

public abstract class CovidReader {

    String inputFileName;
    private HashMap<Integer, CovidData> covidData;

    public CovidReader(String fileName) {
        this.inputFileName = fileName;
    }

    public abstract HashMap<Integer, HashMap<Date, CovidData>> getCovidData() throws Exception;

}
