package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;

import java.util.List;

public class PropertyAreaAverage extends PropertyAverage {


    PropertyAreaAverage(List<PropertyData> propertyDataList,int zip) {
        super(propertyDataList,zip);
    }

    public int getPropertySum(int zip) {
        int totalArea=0;
        for (PropertyData propertyData:this.propertyDataList) {
            if (propertyData.getTotalLivableArea() != -1000) {
                if (propertyData.getZip()==zip) {
                    totalArea+=propertyData.getTotalLivableArea();
                }
            }
        }
        return totalArea;
    }
}
