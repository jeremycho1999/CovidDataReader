package edu.upenn.cit594.util;

public class PropertyData {
    private int zip;
    private Double marketValue;
    private Double totalLivableArea;

    public PropertyData(int zip, double marketValue, double totalLivableArea) throws Exception {
        this.zip = zip;
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
    }
    public int getZip() {return zip;}
    public Double getMarketValue() {
        return marketValue;
    }

    public Double getTotalLivableArea() {
        return totalLivableArea;
    }
}
