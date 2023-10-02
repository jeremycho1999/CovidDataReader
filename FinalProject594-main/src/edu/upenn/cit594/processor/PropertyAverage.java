package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyData;

import java.util.HashMap;
import java.util.List;

public abstract class PropertyAverage {

    private final int zip;
    protected List<PropertyData> propertyDataList;
    private final int propertySum;
    private final int averageProperty;

    PropertyAverage(List<PropertyData> propertyDataList, int zip) {

        this.zip=zip;
        this.propertyDataList=propertyDataList;
        this.propertySum=getPropertySum(zip);
        this.averageProperty= setAverageProperty();
    }

    private int setAverageProperty() {
        int propertyCount = 0;
        for (PropertyData propertyData:this.propertyDataList) {
            if (propertyData.getZip()==zip) {
                propertyCount++;
            }
        }
        if (propertyCount==0){
            return 0;
        }
        return propertySum/propertyCount;
    }

    protected abstract int getPropertySum(int zip);

    public int getAverageProperty() {
        return averageProperty;
    }
}
