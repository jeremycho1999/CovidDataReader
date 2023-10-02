package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.DataManager;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PropertyData;
import edu.upenn.cit594.util.VaccinationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DataProcessorTest {

    DataProcessor dataProcessor;

    HashMap<Integer, Integer> populationData;
    HashMap<Integer, HashMap<Date, CovidData>> covidData;

    CovidData covidData_1_Set_A;
    CovidData covidData_2_Set_A;
    CovidData covidData_3_Set_A;

    CovidData covidData_1_Set_B;
    CovidData covidData_2_Set_B;
    CovidData covidData_3_Set_B;

    List<PropertyData> propertyData;

    PropertyData propertyData_1_Set_A;
    PropertyData propertyData_2_Set_A;
    PropertyData propertyData_3_Set_A;

    PropertyData propertyData_1_Set_B;
    PropertyData propertyData_2_Set_B;
    PropertyData propertyData_3_Set_B;

    int ZIP_1=10;
    int ZIP_2=20;
    int ZIP_3=30;

    int POPULATION_1=1000;
    int POPULATION_2=2000;
    int POPULATION_3=3000;

    double FULLY_VACCINATED_1_Set_A=120;
    double FULLY_VACCINATED_2_Set_A=140;
    double FULLY_VACCINATED_3_Set_A=180;

    double FULLY_VACCINATED_1_Set_B=80;
    double FULLY_VACCINATED_2_Set_B=90;
    double FULLY_VACCINATED_3_Set_B=100;

    double PARTIALLY_VACCINATED_1_Set_A=50;
    double PARTIALLY_VACCINATED_2_Set_A=60;
    double PARTIALLY_VACCINATED_3_Set_A=70;

    double PARTIALLY_VACCINATED_1_Set_B=100;
    double PARTIALLY_VACCINATED_2_Set_B=120;
    double PARTIALLY_VACCINATED_3_Set_B=140;

    double HOSPITALIZED_1_Set_A=200;
    double HOSPITALIZED_2_Set_A=300;
    double HOSPITALIZED_3_Set_A=400;

    double HOSPITALIZED_1_Set_B=100;
    double HOSPITALIZED_2_Set_B=500;
    double HOSPITALIZED_3_Set_B=600;

    //date 1 (3-25)
    String dateString_Set_A ="2021-03-25";
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
    Date DATE_Set_A = simpleDateFormat.parse(dateString_Set_A);

    //date 2 (3-26)
    String dateString_Set_B="2021-03-26";
    SimpleDateFormat simpleDateFormat2= new SimpleDateFormat("yyyy-MM-dd");
    Date DATE_Set_B = simpleDateFormat2.parse(dateString_Set_B);

//    //date 3 (3-27)
//    String dateString_3_Set_A="2021-03-27";
//    SimpleDateFormat simpleDateFormat3= new SimpleDateFormat("yyyy-MM-dd");
//    Date DATE_3_Set_A= simpleDateFormat3.parse(dateString_3_Set_A);
//
//    //date 1 (3-25)
//    String dateString_1_Set_B="2021-03-28";
//    SimpleDateFormat simpleDateFormat4= new SimpleDateFormat("yyyy-MM-dd");
//    Date DATE_1_Set_B= simpleDateFormat4.parse(dateString_1_Set_B);
//
//    //date 2 (3-26)
//    String dateString_2_Set_B="2021-03-29";
//    SimpleDateFormat simpleDateFormat5= new SimpleDateFormat("yyyy-MM-dd");
//    Date DATE_2_Set_B= simpleDateFormat5.parse(dateString_2_Set_B);
//
//    //date 3 (3-27)
//    String dateString_3_Set_B="2021-03-30";
//    SimpleDateFormat simpleDateFormat6= new SimpleDateFormat("yyyy-MM-dd");
//    Date DATE_3_Set_B= simpleDateFormat6.parse(dateString_3_Set_B);

    int MARKET_VALUE_1_Set_A=500;
    int MARKET_VALUE_2_Set_A=600;
    int MARKET_VALUE_3_Set_A=700;

    int MARKET_VALUE_1_Set_B=1000;
    int MARKET_VALUE_2_Set_B=2434;
    int MARKET_VALUE_3_Set_B=5454;

    int LIVABLE_AREA_1_Set_A= 25;
    int LIVABLE_AREA_2_Set_A= 35;
    int LIVABLE_AREA_3_Set_A= 45;

    int LIVABLE_AREA_1_Set_B=4343;
    int LIVABLE_AREA_2_Set_B= 354;
    int LIVABLE_AREA_3_Set_B= 111;

    DataProcessorTest() throws ParseException {
    }

    @BeforeEach
    void setUp() throws Exception {

        setUpPopulation();
        setUpCovidData();
        setUpPropertyData();

        setUpDataProcessor();

    }

    private void setUpDataProcessor() {

        dataProcessor= new DataProcessor();

        dataProcessor.covidData=covidData;
        dataProcessor.populationData=populationData;
        dataProcessor.propertyData=propertyData;

        dataProcessor.fullVaccinationPerCapitaForDateMap= new TreeMap<>();
        dataProcessor.partialVaccinationPerCapitaForDateMap= new TreeMap<>();

        dataProcessor.marketValueForZipMap= new TreeMap<>();
        dataProcessor.areaForZipMap= new TreeMap<>();

        dataProcessor.marketValuePerCapitaForZipMap= new TreeMap<>();

        dataProcessor.hospitalizedPerCapitaForDateMap= new TreeMap<>();
    }

    private void setUpPropertyData() throws Exception {

        propertyData= new ArrayList<>();

        propertyData_1_Set_A= new PropertyData(ZIP_1,MARKET_VALUE_1_Set_A,LIVABLE_AREA_1_Set_A);
        propertyData_2_Set_A= new PropertyData(ZIP_2,MARKET_VALUE_2_Set_A,LIVABLE_AREA_2_Set_A);
        propertyData_3_Set_A= new PropertyData(ZIP_3,MARKET_VALUE_3_Set_A,LIVABLE_AREA_3_Set_A);

        propertyData_1_Set_B= new PropertyData(ZIP_1,MARKET_VALUE_1_Set_B,LIVABLE_AREA_1_Set_B);
        propertyData_2_Set_B= new PropertyData(ZIP_2,MARKET_VALUE_2_Set_B,LIVABLE_AREA_2_Set_B);
        propertyData_3_Set_B= new PropertyData(ZIP_3,MARKET_VALUE_3_Set_B,LIVABLE_AREA_3_Set_B);

        propertyData.addAll(Arrays.asList(
                propertyData_1_Set_A, propertyData_1_Set_B,
                propertyData_2_Set_A, propertyData_2_Set_B,
                propertyData_3_Set_A, propertyData_3_Set_B));

    }

    private void setUpCovidData() throws ParseException {

        covidData= new HashMap<>();

        //first set
        covidData_1_Set_A= new CovidData(DATE_Set_A,PARTIALLY_VACCINATED_1_Set_A,FULLY_VACCINATED_1_Set_A,
                HOSPITALIZED_1_Set_A);
        covidData_2_Set_A= new CovidData(DATE_Set_A,PARTIALLY_VACCINATED_2_Set_A,FULLY_VACCINATED_2_Set_A,
                HOSPITALIZED_2_Set_A);
        covidData_3_Set_A= new CovidData(DATE_Set_A,PARTIALLY_VACCINATED_3_Set_A,FULLY_VACCINATED_3_Set_A,
                HOSPITALIZED_3_Set_A);

        covidData_1_Set_B= new CovidData(DATE_Set_B,PARTIALLY_VACCINATED_1_Set_B,FULLY_VACCINATED_1_Set_B,
                HOSPITALIZED_1_Set_B);
        covidData_2_Set_B= new CovidData(DATE_Set_B,PARTIALLY_VACCINATED_2_Set_B,FULLY_VACCINATED_2_Set_B,
                HOSPITALIZED_2_Set_B);
        covidData_3_Set_B= new CovidData(DATE_Set_B,PARTIALLY_VACCINATED_3_Set_B,FULLY_VACCINATED_3_Set_B,
                HOSPITALIZED_3_Set_B);

        HashMap<Date, CovidData> covidDataArrayList1 = new HashMap<>();
        covidDataArrayList1.put(DATE_Set_A,covidData_1_Set_A);
        covidDataArrayList1.put(DATE_Set_B,covidData_1_Set_B);

        HashMap<Date, CovidData> covidDataArrayList2 = new HashMap<>();
        covidDataArrayList2.put(DATE_Set_A,covidData_2_Set_A);
        covidDataArrayList2.put(DATE_Set_B,covidData_2_Set_B);

        HashMap<Date, CovidData> covidDataArrayList3 = new HashMap<>();
        covidDataArrayList3.put(DATE_Set_A,covidData_3_Set_A);
        covidDataArrayList3.put(DATE_Set_B,covidData_3_Set_B);

        covidData.put(ZIP_1,covidDataArrayList1);
        covidData.put(ZIP_2,covidDataArrayList2);
        covidData.put(ZIP_3,covidDataArrayList3);
    }

    private void setUpPopulation() {

        populationData= new HashMap<>();

        populationData.put(ZIP_1,POPULATION_1);
        populationData.put(ZIP_2,POPULATION_2);
        populationData.put(ZIP_3,POPULATION_3);
    }

    @Test
    void getTotalPopulationForAllZip() {

        int EXPECTED_TOTAL_POPULATION_FOR_ALL_ZIP=6000; //1000 +2000+ 3000
        assertEquals(EXPECTED_TOTAL_POPULATION_FOR_ALL_ZIP,dataProcessor.getTotalPopulationForAllZip());

        //test memo by seeing size
        assertNotEquals(dataProcessor.getTotalPopulationForAllZip(), 0);

        //test memoization by accessing field
        int MOCK_TOTAL_POPULATION= 15;
        dataProcessor.totalPopulationForAllZip=MOCK_TOTAL_POPULATION;
        assertEquals(MOCK_TOTAL_POPULATION,dataProcessor.getTotalPopulationForAllZip());

        //Given test with other File
        DataProcessor dataProcessor1= new DataProcessor(new DataManager(null,"src/population.csv",null));
        int EXPECTED_TOTAL_POP= 1603797;
        assertEquals(EXPECTED_TOTAL_POP,dataProcessor1.getTotalPopulationForAllZip());
    }

    @Test
    void getPartialVaccinationsPerCapita() {

        TreeMap<Integer,Double> zipWithDates= new TreeMap<>();

        double Zip_1_VaccinationPerCapita =
                 Math.round(covidData_1_Set_A.getPartiallyVaccinated()/populationData.get(ZIP_1) * 10000.0)/ 10000.0;
        zipWithDates.put(ZIP_1,Zip_1_VaccinationPerCapita);

        double Zip_2_VaccinationPerCapita =
                Math.round(covidData_2_Set_A.getPartiallyVaccinated()/populationData.get(ZIP_2) * 10000.0)/ 10000.0;
        zipWithDates.put(ZIP_2,Zip_2_VaccinationPerCapita);

        double Zip_3_VaccinationPerCapita =
                Math.round(covidData_3_Set_A.getPartiallyVaccinated()/populationData.get(ZIP_3) * 10000.0)/ 10000.0;
        zipWithDates.put(ZIP_3,Zip_3_VaccinationPerCapita);

        assertEquals(zipWithDates,dataProcessor.getVaccinationsPerCapita(VaccinationStatus.PARTIAL,DATE_Set_A));

    }

    @Test
    void getFullVaccinationsPerCapita() {
        TreeMap<Integer,Double> zipWithDates= new TreeMap<>();

        double Zip_1_VaccinationPerCapita =
                Math.round(covidData_1_Set_A.getFullyVaccinated()/populationData.get(ZIP_1) * 10000.0)/ 10000.0;
        zipWithDates.put(ZIP_1,Zip_1_VaccinationPerCapita);

        double Zip_2_VaccinationPerCapita =
                Math.round(covidData_2_Set_A.getFullyVaccinated()/populationData.get(ZIP_2) * 10000.0)/ 10000.0;
        zipWithDates.put(ZIP_2,Zip_2_VaccinationPerCapita);

        double Zip_3_VaccinationPerCapita =
                Math.round(covidData_3_Set_A.getFullyVaccinated()/populationData.get(ZIP_3) * 10000.0)/ 10000.0;
        zipWithDates.put(ZIP_3,Zip_3_VaccinationPerCapita);

        assertEquals(zipWithDates,dataProcessor.getVaccinationsPerCapita(VaccinationStatus.FULL,DATE_Set_A));
    }

    @Test
    void getAverageMarketValueForZip() {

        //standard cases

        double AMOUNT_OF_PROPERTIES=2;

        int zip=10;
        double totalMarketValue= propertyData_1_Set_A.getMarketValue()+ propertyData_1_Set_B.getMarketValue();
        int averageMarketValue= (int) (totalMarketValue/AMOUNT_OF_PROPERTIES);

        assertEquals(averageMarketValue,dataProcessor.getAverageMarketValueForZip(zip));

        zip=20;
        totalMarketValue= propertyData_2_Set_A.getMarketValue()+ propertyData_2_Set_B.getMarketValue();
        averageMarketValue= (int) (totalMarketValue/AMOUNT_OF_PROPERTIES);

        assertEquals(averageMarketValue,dataProcessor.getAverageMarketValueForZip(zip));

        zip=30;
        totalMarketValue= propertyData_3_Set_A.getMarketValue()+ propertyData_3_Set_B.getMarketValue();
        averageMarketValue= (int) (totalMarketValue/AMOUNT_OF_PROPERTIES);

        assertEquals(averageMarketValue,dataProcessor.getAverageMarketValueForZip(zip));

        //test memo by looking at current map size
        assertEquals(3,dataProcessor.marketValueForZipMap.size());

        //test memo by setting field
        zip=10;
        int mockAverage= 55;
        dataProcessor.marketValueForZipMap.put(10,mockAverage);

        assertEquals(mockAverage,dataProcessor.getAverageMarketValueForZip(zip));

        zip=20;
        mockAverage= 85;
        dataProcessor.marketValueForZipMap.put(20,mockAverage);

        assertEquals(mockAverage,dataProcessor.getAverageMarketValueForZip(zip));

    }

    @Test
    void getAverageLivableAreaForZip() {

        //standard cases

        double AMOUNT_OF_PROPERTIES=2;

        int zip=10;
        double totalLivable= propertyData_1_Set_A.getTotalLivableArea()+ propertyData_1_Set_B.getTotalLivableArea();
        int averageArea= (int) (totalLivable/AMOUNT_OF_PROPERTIES);

        assertEquals(averageArea,dataProcessor.getAverageLivableAreaForZip(zip));

        zip=20;
        totalLivable= propertyData_2_Set_A.getTotalLivableArea()+ propertyData_2_Set_B.getTotalLivableArea();
        averageArea= (int) (totalLivable/AMOUNT_OF_PROPERTIES);

        assertEquals(averageArea,dataProcessor.getAverageLivableAreaForZip(zip));

        zip=30;
        totalLivable= propertyData_3_Set_A.getTotalLivableArea()+ propertyData_3_Set_B.getTotalLivableArea();
        averageArea= (int) (totalLivable/AMOUNT_OF_PROPERTIES);

        assertEquals(averageArea,dataProcessor.getAverageLivableAreaForZip(zip));

        //test memoization by looking at current map size
        assertEquals(3, dataProcessor.areaForZipMap.size());

        //memoization by setting field
        zip=10;
        int mockAverage= 55;
        dataProcessor.areaForZipMap.put(10,mockAverage);

        assertEquals(mockAverage,dataProcessor.getAverageLivableAreaForZip(zip));

        zip=20;
        mockAverage= 85;
        dataProcessor.areaForZipMap.put(20,mockAverage);

        assertEquals(mockAverage,dataProcessor.getAverageLivableAreaForZip(zip));
    }

    @Test
    void getMarketValuePerCapita() {

        //standard cases

        int zip=10;
        double totalValueOfProperties= propertyData_1_Set_A.getMarketValue()+propertyData_1_Set_B.getMarketValue();
        int populationOfZip=POPULATION_1;

        int marketValuePerCapita= (int) (totalValueOfProperties/populationOfZip);
        assertEquals(marketValuePerCapita,dataProcessor.getMarketValuePerCapita(zip));

        zip=20;
        totalValueOfProperties= propertyData_2_Set_A.getMarketValue()+propertyData_2_Set_B.getMarketValue();
        populationOfZip=POPULATION_2;

        marketValuePerCapita= (int) (totalValueOfProperties/populationOfZip);
        assertEquals(marketValuePerCapita,dataProcessor.getMarketValuePerCapita(zip));

        zip=30;
        totalValueOfProperties= propertyData_3_Set_A.getMarketValue()+propertyData_3_Set_B.getMarketValue();
        populationOfZip= POPULATION_3;

        marketValuePerCapita= (int) (totalValueOfProperties/populationOfZip);
        assertEquals(marketValuePerCapita,dataProcessor.getMarketValuePerCapita(zip));

        //test memoization by looking at current map size
        assertEquals(3, dataProcessor.marketValuePerCapitaForZipMap.size());

        //memoization by accessing fields
        zip=10;
        int mockMarketValuePerCapita= 434;
        dataProcessor.marketValuePerCapitaForZipMap.put(zip,mockMarketValuePerCapita);
        assertEquals(mockMarketValuePerCapita,dataProcessor.getMarketValuePerCapita(zip));

        zip=20;
        mockMarketValuePerCapita= 90;
        dataProcessor.marketValuePerCapitaForZipMap.put(zip,mockMarketValuePerCapita);
        assertEquals(mockMarketValuePerCapita,dataProcessor.getMarketValuePerCapita(zip));

        zip=30;
        mockMarketValuePerCapita= 488;
        dataProcessor.marketValuePerCapitaForZipMap.put(zip,mockMarketValuePerCapita);
        assertEquals(mockMarketValuePerCapita,dataProcessor.getMarketValuePerCapita(zip));

    }
}