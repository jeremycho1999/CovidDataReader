package edu.upenn.cit594.util;

import java.util.Date;

public class CovidData{
    Date date;
    double partiallyVaccinated;
    double fullyVaccinated;
    double hospitalized;

    public CovidData(Date date, double partiallyVaccinated, double fullyVaccinated, double hospitalized) {
        this.date= date;
        this.partiallyVaccinated = partiallyVaccinated;
        this.fullyVaccinated = fullyVaccinated;
        this.hospitalized = hospitalized;
    }

    public Date getDate() {
        return date;
    }

    public double getPartiallyVaccinated() {
        return partiallyVaccinated;
    }

    public double getFullyVaccinated() {
        return fullyVaccinated;
    }
    public double getHospitalized() {
        return hospitalized;
    }
}
