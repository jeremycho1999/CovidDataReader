package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.VaccinationStatus;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public interface KeyInquires {

    public int getTotalPopulationForAllZip();

    public SortedMap<Integer,Double> getVaccinationsPerCapita(VaccinationStatus vaccinationStatus, Date date);

    public int getAverageMarketValueForZip(int zip);

    public int getAverageLivableAreaForZip(int zip);

    public int getMarketValuePerCapita(int zip);

    //public  getAddedFeature
    public SortedMap<Integer, Double> getHospitalizedPerCapitaSortedByMarketValue(Date date);

}
