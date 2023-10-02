package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PropertyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationReaderTest {
    private PopulationReader populationReader;
    private static final String TEST_FILENAME = "src/PopulationTestData.csv";
    private static final String REAL_FILENAME = "src/population.csv";

    @BeforeEach
    void setUp() {
        populationReader = new PopulationReader(TEST_FILENAME);
    }

    @Test
    void testGetPopulationData() {
        // Test that the correct population data is returned
        assertEquals(15190, populationReader.getPopulationData().get(19106));
        assertEquals(3, populationReader.getPopulationData().get(19112));

        // Test that the HashMap is not empty
        assertFalse(populationReader.getPopulationData().isEmpty());

        // Test that an empty HashMap is returned if the input file does not exist
        PopulationReader invalidPopulationReader = new PopulationReader("invalid_population_file.csv");
        assertTrue(invalidPopulationReader.getPopulationData().isEmpty());
    }

    @Test
    void testGetPopulationData_InvalidZip() {
        // Test that a zip code that is not present in the file is not in the population data HashMap
        assertFalse(populationReader.getPopulationData().containsKey(1234));
    }
    @Test
    void testGetPopulationData_InvalidPopulation() {
        // Test that a zip code that is not present in the file is not in the population data HashMap
        assertFalse(populationReader.getPopulationData().containsKey(20000));
    }
    @Test
    void testCodeWorksAndIsEfficientOnReadData() throws Exception {
        long startTime=System.currentTimeMillis();
        PopulationReader populationReader= new PopulationReader(REAL_FILENAME);
        HashMap<Integer,Integer> populationMap= populationReader.getPopulationData();
        long endTime=System.currentTimeMillis();
        System.out.println((endTime-startTime)/1000);
        assertTrue((endTime-startTime) /1000<5);
    }
}