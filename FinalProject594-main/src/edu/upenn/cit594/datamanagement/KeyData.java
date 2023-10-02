package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PropertyData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface KeyData {

    public HashMap<Integer, Integer> getPopulationData();

    public HashMap<Integer, HashMap<Date, CovidData>> getCovidData();

    public List<PropertyData> getPropertyData();

}
