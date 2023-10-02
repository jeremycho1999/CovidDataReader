package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CSVCovidReaderTest {

    String covidTestFileName="src/MyTestCovidData.csv";
    String swappedCovidTestFileName = "src/swappedColsCovid.csv";
    CovidReader csvCovidReader= new CSVCovidReader(swappedCovidTestFileName);
    String REAL_FILE_NAME_CSV= "src/covid_data.csv";

    HashMap<Integer, HashMap<Date, CovidData>> covidDataHashMap= csvCovidReader.getCovidData();
    int ZIP_IN_MAP;
    int  ZIP_NOT_IN_MAP;

    String DATE_TO_CHECK;

    CSVCovidReaderTest() throws Exception {
    }

    @org.junit.jupiter.api.Test
    void basicTestContainsZipAsKey() {

        ZIP_IN_MAP= 19103;
        assertTrue(covidDataHashMap.containsKey(ZIP_IN_MAP));

        ZIP_IN_MAP= 19103;
        assertTrue(covidDataHashMap.containsKey(ZIP_IN_MAP));

        ZIP_NOT_IN_MAP= 200;
        assertFalse(covidDataHashMap.containsKey(ZIP_NOT_IN_MAP));

    }

    @org.junit.jupiter.api.Test
    void basicTestContainsVaccinationValues() throws ParseException {

        ZIP_IN_MAP= 19103;

        int INDEX_OF_ZIP = 3;
        String DATE_OF_ZIP_19103= "2021-03-25";
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate= simpleDateFormat.parse(DATE_OF_ZIP_19103);

        int PARTIALLY_VACC_FOR_19013= 4844;
        int FULLY_VACC_FOR_19013= 5696;
        int HOSPITALIZED_FOR_1903 = 128;

        assertEquals(PARTIALLY_VACC_FOR_19013, covidDataHashMap.get(ZIP_IN_MAP).get(convertedDate).getPartiallyVaccinated());
        assertEquals(FULLY_VACC_FOR_19013, covidDataHashMap.get(ZIP_IN_MAP).get(convertedDate).getFullyVaccinated());
        assertEquals(HOSPITALIZED_FOR_1903, covidDataHashMap.get(ZIP_IN_MAP).get(convertedDate).getHospitalized());

    }

    @org.junit.jupiter.api.Test
    void basicTestDate() throws ParseException {

        ZIP_IN_MAP= 19103;
        int INDEX_OF_ZIP = 3;
        String DATE_OF_ZIP_19103= "2021-03-25";
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate= simpleDateFormat.parse(DATE_OF_ZIP_19103);

        String dateFromFile= simpleDateFormat.format(covidDataHashMap.get(ZIP_IN_MAP).get(convertedDate).getDate());

        assertEquals(DATE_OF_ZIP_19103,dateFromFile);
    }

    @org.junit.jupiter.api.Test
    void testIgnoreRecordWithZipWithLessThanFive() {

        assertFalse(covidDataHashMap.containsKey(194),"Program should ignore entries with zip not five digits");
    }

    //Not sure if we need to test for this
//    @org.junit.jupiter.api.Test
//    void testIgnoreRecordWithZipAsString() {
//
//        assertFalse(covidDataHashMap.containsKey(12045),"Program should ignore entries with zip as string");
//    }

    @org.junit.jupiter.api.Test
    void testIgnoreRecordWithTimeStampWithoutDashes() {

        assertFalse(covidDataHashMap.containsKey(12046),"Program should ignore entries with timeStamp without dashes");
    }

    @org.junit.jupiter.api.Test
    void doNotIgnoreRecordsWithMissingVaccination() {

        assertTrue(covidDataHashMap.containsKey(19100),"Program should still process records with missing " +
                "vaccinations");
    }

    @Test
    void testCodeWorksAndIsEfficientOnReadData() throws Exception {
        long startTime=System.currentTimeMillis();
        CovidReader covidReader= new CSVCovidReader(REAL_FILE_NAME_CSV);
        HashMap<Integer, HashMap<Date, CovidData>> covidData= covidReader.getCovidData();
        System.out.println(covidData.size());
        long endTime=System.currentTimeMillis();
        assertTrue((endTime-startTime) /1000<20);
    }
}