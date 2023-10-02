package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;

import java.util.List;

public class PropertyMarketAverage extends PropertyAverage {


    PropertyMarketAverage(List<PropertyData> propertyDataList,int zip) {
        super(propertyDataList,zip);
    }

    public int getPropertySum(int zip) {
        int totalValue=0;
        for (PropertyData propertyData:this.propertyDataList) {
            if (propertyData.getMarketValue() != -1000 ) {
                if (propertyData.getZip()==zip) {
                    totalValue+=propertyData.getMarketValue();
                }
            }
        }
        return totalValue;
    }
}
