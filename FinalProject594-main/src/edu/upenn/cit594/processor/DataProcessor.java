package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.DataManager;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PropertyData;
import edu.upenn.cit594.util.VaccinationStatus;

import java.util.*;

public class DataProcessor implements KeyInquires {

    HashMap<Integer, Integer> populationData;
    HashMap<Integer, HashMap<Date, CovidData>> covidData;

    List<PropertyData> propertyData;

    //For memoization purposes

    //For total population inquiry
    protected int totalPopulationForAllZip;

    //For Vaccination Per Capita Inquiry
    protected Map<Date,SortedMap<Integer,Double>> partialVaccinationPerCapitaForDateMap;
    protected Map<Date,SortedMap<Integer,Double>> fullVaccinationPerCapitaForDateMap;

    //For average market value inquiry //change
    Map<Integer,Integer> marketValueForZipMap;

    //For average area inquiry
    Map<Integer,Integer> areaForZipMap;

    //For market value per capita
    Map<Integer,Integer> marketValuePerCapitaForZipMap;

    //For hospitalizations per capita
    Map<Date,SortedMap<Integer, Double>> hospitalizedPerCapitaForDateMap;

    public DataProcessor(DataManager dataManager) {

        this.populationData= dataManager.getPopulationData();
        this.covidData= dataManager.getCovidData();
        this.propertyData= dataManager.getPropertyData();

        this.fullVaccinationPerCapitaForDateMap= new TreeMap<>();
        this.partialVaccinationPerCapitaForDateMap= new TreeMap<>();

        this.marketValueForZipMap= new TreeMap<>();
        this.areaForZipMap= new TreeMap<>();

        this.marketValuePerCapitaForZipMap= new TreeMap<>();

        this.hospitalizedPerCapitaForDateMap= new TreeMap<>();

    }

    //for testing
    public DataProcessor() {

    }

    @Override
    public int getTotalPopulationForAllZip() {

        //memoization
        if (this.totalPopulationForAllZip!=0)
            return this.totalPopulationForAllZip;

        int populationTotalResults = 0;
        for (Integer population : populationData.values()) {
            populationTotalResults += population;
        }

        this.totalPopulationForAllZip=populationTotalResults;

        return populationTotalResults;
    }

    @Override
    public SortedMap<Integer, Double> getVaccinationsPerCapita(VaccinationStatus vaccinationStatus, Date date) {

        if (vaccinationStatus.name().equals(VaccinationStatus.PARTIAL.name()))
            return getPartialVaccinationsPerCapita(date);

        return getFullVaccinationsPerCapita(date);
    }

    private SortedMap<Integer, Double> getFullVaccinationsPerCapita(Date date) {

        // memoization
        if (this.fullVaccinationPerCapitaForDateMap.containsKey(date)) {
            return fullVaccinationPerCapitaForDateMap.get(date);
        }

        TreeMap<Integer, Double> vaccinationsPerCapita = new TreeMap<>();

        for (Map.Entry<Integer, HashMap<Date, CovidData>> entry : covidData.entrySet()) {
            HashMap<Date, CovidData> covidDataList = entry.getValue();
            for (CovidData covidData : covidDataList.values()) {
                if (covidData.getDate().toString().equals(date.toString()) && this.populationData.containsKey(entry.getKey())) {
                    int population = this.populationData.get(entry.getKey());
                    double vaxRate = Math.round(covidData.getFullyVaccinated() / population * 10000.0) / 10000.0;
                    vaccinationsPerCapita.put(entry.getKey(), vaxRate);
                }
            }
        }

        this.fullVaccinationPerCapitaForDateMap.put(date, vaccinationsPerCapita);

        return vaccinationsPerCapita;
    }

    private SortedMap<Integer, Double> getPartialVaccinationsPerCapita(Date date) {

        //memoization
        if (this.partialVaccinationPerCapitaForDateMap.containsKey(date)) {
            return partialVaccinationPerCapitaForDateMap.get(date);
        }

        TreeMap<Integer, Double> vaccinationsPerCapita = new TreeMap<>();

        for (Map.Entry<Integer, HashMap<Date, CovidData>> entry : covidData.entrySet()) {
            HashMap<Date, CovidData> covidDataList = entry.getValue();
            for (CovidData covidData : covidDataList.values()) {
                if (covidData.getDate().toString().equals(date.toString())&& this.populationData.containsKey(entry.getKey())) {
                    int population = this.populationData.get(entry.getKey());
                    double vaxRate = Math.round(covidData.getPartiallyVaccinated() / population * 10000.0) / 10000.0;
                    vaccinationsPerCapita.put(entry.getKey(), vaxRate);
                }
            }
        }

        this.partialVaccinationPerCapitaForDateMap.put(date,vaccinationsPerCapita);

        return vaccinationsPerCapita;

    }


    //For next two methods, we make use of Strategy Pattern

    @Override
    public int getAverageMarketValueForZip(int zip) {

        //memoization
        if (this.marketValueForZipMap.containsKey(zip))
            return this.marketValueForZipMap.get(zip);

        PropertyAverage propertyMarketAverage= new PropertyMarketAverage(propertyData,zip);
        this.marketValueForZipMap.put(zip,propertyMarketAverage.getAverageProperty());

        return propertyMarketAverage.getAverageProperty();
    }

    @Override
    public int getAverageLivableAreaForZip(int zip) {

        //memoization
        if (this.areaForZipMap.containsKey(zip))
            return this.areaForZipMap.get(zip);

        PropertyAverage propertyAreaSum= new PropertyAreaAverage(propertyData,zip);
        this.areaForZipMap.put(zip,propertyAreaSum.getAverageProperty());

        return propertyAreaSum.getAverageProperty();
    }

    @Override
    public int getMarketValuePerCapita(int zip) {

        if (this.marketValuePerCapitaForZipMap.containsKey(zip)) {
            return marketValuePerCapitaForZipMap.get(zip);
        }

        //avoid dividing be 0 in case no population info for given zip
        //second part of condition may be extra depending on what the file looks like
        if(!populationData.containsKey(zip) || populationData.get(zip)==0)
            return 0;

        int marketValuePerCapita = getTotalMarketValue(zip)/populationData.get(zip);
        this.marketValuePerCapitaForZipMap.put(zip,marketValuePerCapita);

        return marketValuePerCapita;
    }

    public SortedMap<Integer, Double> getHospitalizedPerCapitaSortedByMarketValue(Date date) {

        if (this.hospitalizedPerCapitaForDateMap.containsKey(date)) {
            return hospitalizedPerCapitaForDateMap.get(date);
        }

        TreeMap<Integer,Double> hospitalizedPerCapita= createMapSortedByAverageMarketValue();

        for (Map.Entry<Integer, HashMap<Date,CovidData>> entry : covidData.entrySet()) {
            HashMap<Date, CovidData> covidDataList = entry.getValue();
            for (CovidData covidData : covidDataList.values()) {
                if (covidData.getDate().toString().equals(date.toString())&& this.populationData.containsKey(entry.getKey())) {
                    Integer population = this.populationData.get(entry.getKey());
                    double vaxRate = Math.round(covidData.getHospitalized() / population * 10000.0) / 10000.0;
                    hospitalizedPerCapita.put(entry.getKey(), vaxRate);
                }
            }
        }
        this.hospitalizedPerCapitaForDateMap.put(date,hospitalizedPerCapita);

        return hospitalizedPerCapita;
    }

    private TreeMap<Integer, Double> createMapSortedByAverageMarketValue() {

        return new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer zip1, Integer zip2) {
                return getAverageMarketValueForZip(zip1)-getAverageMarketValueForZip(zip2);
            }
        });
    }

//    @Override
//    public SortedMap<Integer, Double> getHospitalizedPerCapitaSortedByMarketValue(Date date) {
//        TreeMap<Integer,Double> hospitalizationsPerCapita= getHospitalizedPerCapita(date);
//        return sortMapBasedOnMarketValue(hospitalizationsPerCapita);
//
//    }
//
//    private Map<Integer, Double> sortMapBasedOnMarketValue(Map<Integer, Double> hospitalizationsPerCapita) {
//        //implement comparator
//        TreeMap<Integer,Double> myMap= new TreeMap<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer zip1, Integer zip2) {
//                return getAverageMarketValueForZip(zip1)-getAverageMarketValueForZip(zip2);
//            }
//        });
//        return null;
//    }

    private int getTotalMarketValue(int zip) {
        int totalMarketValue = 0;
        for (PropertyData propertyData : this.propertyData) {
            if (propertyData.getMarketValue() != Double.NEGATIVE_INFINITY) {
                if (propertyData.getZip()==zip && propertyData.getMarketValue()!=null)
                    totalMarketValue += propertyData.getMarketValue();
            }
        }
        return totalMarketValue;
    }

    //UI tier will need this information to know available questions

    public boolean doesCovidDataExist() {
        return covidData!=null;
    }

    public boolean doesPropertyDataExist() {
        return propertyData!=null;
    }

    public boolean doesPopulationDataExist() {
        return populationData!=null;
    }
}
