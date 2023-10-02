package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PropertyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyReaderTest {

    PropertyReader propertyReader;
    private static final String TEST_FILENAME = "src/PropertyTestData.csv";
    private static final String REAL_FILENAME = "src/properties.csv";

    @BeforeEach
    void setUp() {
        propertyReader = new PropertyReader(TEST_FILENAME);
    }

    @Test
    void testGetPropertyData() throws Exception {
        // Test that the size of the list returned by getPropertyData() is correct
        List<PropertyData> propertyDataList = propertyReader.getPropertyData();
        assertEquals(8, propertyDataList.size());

        // Test that the values of the first and last PropertyData objects in the list are correct
        PropertyData firstPropertyData = propertyDataList.get(0);
        assertEquals(19103, firstPropertyData.getZip());
        assertEquals(1000000.0, firstPropertyData.getMarketValue());
        assertEquals(2000.0, firstPropertyData.getTotalLivableArea());

        PropertyData lastPropertyData = propertyDataList.get(propertyDataList.size() - 1);
        assertEquals(20000, lastPropertyData.getZip());
        assertEquals(400000.0, lastPropertyData.getMarketValue());
        assertEquals(1000.0, lastPropertyData.getTotalLivableArea());
    }

    @Test
    void testGetPropertyData_invalidData() throws Exception {
        // Test that PropertyData objects with invalid data are not added to the list
        List<PropertyData> propertyDataList = propertyReader.getPropertyData();
        assertEquals(8, propertyDataList.size());

        // Test that PropertyData objects with a market value or total livable area of -1000 are not added to the list
        PropertyData invalidMarketValueData = propertyDataList.get(4);
        assertEquals(19104, invalidMarketValueData.getZip());
        assertEquals(-1000.0, invalidMarketValueData.getMarketValue());
        assertEquals(2000.0, invalidMarketValueData.getTotalLivableArea());

        PropertyData invalidTotalLivableAreaData = propertyDataList.get(5);
        assertEquals(19106, invalidTotalLivableAreaData.getZip());
        assertEquals(700000.0, invalidTotalLivableAreaData.getMarketValue());
        assertEquals(-1000.0, invalidTotalLivableAreaData.getTotalLivableArea());
    }

    @Test
    void testCodeWorksAndIsEfficientOnReadData() throws Exception {
        long startTime=System.currentTimeMillis();
        PropertyReader propertyReader = new PropertyReader(REAL_FILENAME);
        List<PropertyData> propertyData= propertyReader.getPropertyData();
        long endTime=System.currentTimeMillis();
        assertTrue((endTime-startTime) /1000<20);
    }
}
