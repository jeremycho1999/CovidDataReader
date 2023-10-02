package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.FileType;
import edu.upenn.cit594.util.PropertyData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DataManager implements KeyData {

    private HashMap<Integer, Integer> populationData;
    private HashMap<Integer, HashMap<Date, CovidData>> covidData;
    private List<PropertyData> propertyData;

    /**
     * Assigns data from various data files to instance variables.
     *
     * @param covidFileName to read
     * @param populationFileName to read
     * @param propertyFileName to read
     */
    public DataManager(String covidFileName, String populationFileName, String propertyFileName) {
        if (populationFileName != null) {
            try {
                PopulationReader populationReader = new PopulationReader(populationFileName);
                this.populationData = populationReader.getPopulationData();
            } catch (Exception e) {
                throw new RuntimeException("Population file not found.");
            }
        }

        if (propertyFileName != null) {
            try {
                PropertyReader propertyReader = new PropertyReader(propertyFileName);
                this.propertyData = propertyReader.getPropertyData();
            } catch (Exception e) {
                throw new RuntimeException("Property file not found.");
            }
        }

        if (covidFileName != null) {
            try {
                FileType covidFileType = getFileType(covidFileName);
                CovidReader covidReader = null;
                switch (covidFileType) {
                    case CSV_FILE:
                        covidReader = new CSVCovidReader(covidFileName);
                        break;
                    case JSON_FILE:
                        covidReader = new JSONCovidReader(covidFileName);
                        break;
                }
                this.covidData = covidReader.getCovidData();
            } catch (Exception e) {
                throw new RuntimeException("Covid File Not Found");
            }
        }
    }

    /**
     * Gets file type from a string of covid file name.
     *
     * @param covidFileName to read.
     * @return FileType
     */
    public static FileType getFileType(String covidFileName) {

        String extension = "";
        int i = covidFileName.lastIndexOf('.');
        if (i > 0 && i < covidFileName.length() - 1) {
            extension = covidFileName.substring(i + 1);
        }
        if (extension.toLowerCase().equals("json")) {
            return FileType.JSON_FILE;
        } else if (extension.toLowerCase().equals("csv")) {
            return FileType.CSV_FILE;
        } else {
            return FileType.NOT_RECOGNIZED;
        }
    }

    @Override
    public HashMap<Integer, Integer> getPopulationData() {
        return this.populationData;
    }

    @Override
    public HashMap<Integer, HashMap<Date, CovidData>> getCovidData() {
        return covidData;
    }

    @Override
    public List<PropertyData> getPropertyData() {
        return propertyData;
    }
}
