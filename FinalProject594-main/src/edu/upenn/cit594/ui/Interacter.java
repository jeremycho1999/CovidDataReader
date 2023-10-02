package edu.upenn.cit594.ui;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.DataProcessor;
import edu.upenn.cit594.util.VaccinationStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Interacter {

    boolean endProgram;

    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(System.in));

    DataProcessor dataProcessor;

    Set<Integer> availableInquiries;

    Logger logger;

    public Interacter(DataProcessor dataProcessor) {

        this.dataProcessor=dataProcessor;
        this.availableInquiries= new HashSet<>();

        logger= Logger.getInstance();

    }

    /**
     * Starts the interacter class, setting available inquiries and starting the program
     *
     * @throws IOException
     */
    public void start() throws IOException {
        setAvailableInquiries();

        while(!endProgram) {
            displayMenu();
            promptUser();
        }
    }

    /**
     * - The available questions depend on the input files
     * Options 0 and 1 will be valid options regardless of input files
     * All other options will depend on the input files.
     */
    private void setAvailableInquiries() {

        this.availableInquiries.add(0);
        this.availableInquiries.add(1);

        //Option 2 concerns the total population
        if (dataProcessor.doesPopulationDataExist())
            this.availableInquiries.add(2);

        //Option 3 concerns the total vaccinations per capita, requiring info from two files
        //Option 7 concerns the total hospitalized per capita, requiring info from two files
        if (dataProcessor.doesCovidDataExist() && dataProcessor.doesPopulationDataExist()) {
            this.availableInquiries.add(3);
        }

        //Options 4 and 5 concerns the average market value and area of properties, respectively.
        if (dataProcessor.doesPropertyDataExist()) {
            this.availableInquiries.add(4);
            this.availableInquiries.add(5);
        }

        //Options 6 concerns the market value of properties per capita, requiring info from two files
        if (dataProcessor.doesPopulationDataExist() && dataProcessor.doesPropertyDataExist())
            this.availableInquiries.add(6);

        //add another for custom feature
        if (dataProcessor.doesPopulationDataExist() && dataProcessor.doesPropertyDataExist() && dataProcessor.doesCovidDataExist())
            this.availableInquiries.add(7);
    }

    /**
     * displays the menu of options
     *
     */
    private void displayMenu() {

        //System.out.println("\nHere is the menu.\n");
        System.out.println("0. Exit the program.");
        System.out.println("1. Show the available actions.");
        System.out.println("2. Show the total population for all ZIP codes.");
        System.out.println("3. Show the total vaccinations per capita for each ZIP code for the specified date. ");
        System.out.println("4. Show the average market value for properties in a specified ZIP code.");
        System.out.println("5. Show the average total livable area for properties in a specified ZIP code.");
        System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP code");
        System.out.println("7. Show the total hospitalized per capita sorted by property market value for each ZIP " +
                "code for " +
                "the specified date. ");

    }

    /**
     * Prompts user to input option selection
     *
     * @throws IOException
     */
    private void promptUser() throws IOException {

        int optionChosen;
        optionChosen= getMenuOptionFromUser();
        logger.writeUserResponses(String.valueOf(optionChosen));

        if (optionChosen==0) {
            endProgram=true;
        }
        else if (optionChosen==1) {
            beginOutputMessage();
            showAvailableOptions();
            endOutputMessage();
        }
        else if (optionChosen==2) {
            beginOutputMessage();
            System.out.println(dataProcessor.getTotalPopulationForAllZip());
            endOutputMessage();
        }
        else if (optionChosen==3) {
            VaccinationStatus vaccinationStatus= getVaccinationStatusFromUser();
            Date date= getDateFromUser();
            Map<Integer,Double> vaxPerCapRequestedMap = dataProcessor.getVaccinationsPerCapita(vaccinationStatus,date);
            beginOutputMessage();
            displayMap(vaxPerCapRequestedMap);
            endOutputMessage();
        }
        else if (optionChosen==4) {
            int zip= getZipFromUser();
            beginOutputMessage();
            System.out.println(dataProcessor.getAverageMarketValueForZip(zip));
            endOutputMessage();
        }
        else if (optionChosen==5) {
            int zip= getZipFromUser();
            beginOutputMessage();
            System.out.println(dataProcessor.getAverageLivableAreaForZip(zip));
            endOutputMessage();
        }
        else if (optionChosen==6) {
            int zip= getZipFromUser();
            beginOutputMessage();
            System.out.println(dataProcessor.getMarketValuePerCapita(zip));
            endOutputMessage();
        }
        else if (optionChosen==7) {
            Date hospitalizedDateRequested = getDateFromUser();
            Map<Integer,Double> hospitalizedMap = dataProcessor.getHospitalizedPerCapitaSortedByMarketValue(hospitalizedDateRequested);
            beginOutputMessage();
            displayMap(hospitalizedMap);
            endOutputMessage();
        }
    }

    /**
     * keeps track of menu options selected by users
     *
     * @return an int of the menu option selected
     * @throws IOException
     */
    private int getMenuOptionFromUser() throws IOException {

        displayInputPromptLine();

        int menuOption = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                String input = bufferedReader.readLine();
                logger.writeUserResponses(input);
                menuOption = Integer.parseInt(input);
                System.out.println(menuOption);
                if (isValidMenuOption(menuOption)) {
                    isValid = true;
                    return menuOption;
                }

            } catch (IOException e) {
                System.out.println("Error reading input. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        bufferedReader.close();
        return menuOption;
    }

    /**
     * Checks if the given response is a valid option in the menu
     *
     * @param response to check
     * @return true or false depending on the value
     */
    private boolean isValidMenuOption(int response) {

        try {
            if (availableInquiries.contains(response)){
                return true;
            }
            else {
                logger.writeUserResponses(String.valueOf(response));
                System.out.println("The number you have chosen is not an available option");
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Please enter an integer value.");
            return false;
        }
    }

    /**
     * Prints out a prompt ">"
     */
    private void displayInputPromptLine() {
        System.out.print("> ");
        System.out.println();
    }

    /**
     * Records the zip code that the user wants to analyze
     *
     * @return zip code
     * @throws IOException
     */
    private int getZipFromUser() throws IOException {

        System.out.println("Please enter a zipcode");
        displayInputPromptLine();
        String response= bufferedReader.readLine();
        while(!isValidInteger(response)) {
            displayInputPromptLine();
            response= bufferedReader.readLine();
        }
        logger.writeUserResponses(response);
        return Integer.parseInt(response);
    }

    /**
     * Checks if the response provided is able to be parsed to an integer
     * @param response to check
     * @return true or false depending on the response
     */
    private boolean isValidInteger(String response) {

        try {
            Integer.parseInt(response);
            return true;
        }
        catch (IllegalArgumentException e) {
            System.out.println("You must enter an integer");
            return false;
        }
    }

    /**
     * Prints every entry in the map
     *
     * @param map
     */
    private void displayMap(Map<Integer, Double> map) {

        for (Map.Entry<Integer,Double> entry:map.entrySet()) {
            System.out.println(entry.getKey() + " " +entry.getValue());
        }
    }

    /**
     * Records the date the user wants to analyze
     *
     * @return date to analyze
     * @throws IOException
     */
    private Date getDateFromUser() throws IOException {

        System.out.println("What date would you like to know about: [yyyy-MM-dd]");
        displayInputPromptLine();
        String response= bufferedReader.readLine();
        while(isValidDate(response) == null) {
            displayInputPromptLine();
            response= bufferedReader.readLine();
        }
        logger.writeUserResponses(response);
        return isValidDate(response);
    }

    /**
     * Checks if the string date provided is valid
     *
     * @param date to check
     * @return date if valid
     */
    private Date isValidDate(String date) {

        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            System.out.println("Please enter a valid date [yyyy-MM-dd]");
            return null;
        }
    }

    /**
     * Records vaccination status user wants to analyze
     *
     * @return VaccinationStatus
     * @throws IOException
     */
    private VaccinationStatus getVaccinationStatusFromUser() throws IOException {
        System.out.println("Which type of vaccinations would you like to know about: [partial/full]");
        displayInputPromptLine();
        String response= bufferedReader.readLine();
        while (!isValidVaccinationType(response)) {
            displayInputPromptLine();
            response= bufferedReader.readLine();
        }
        logger.writeUserResponses(response);
        return VaccinationStatus.valueOf(response.toUpperCase());
    }

    /**
     * Checks if the given response is a valid vaccination status
     *
     * @param response
     * @return
     */
    private boolean isValidVaccinationType(String response) {

        try {
            VaccinationStatus.valueOf(response.toUpperCase());
            return true;
        }
        catch (IllegalArgumentException e) {
            System.out.println("Please enter valid vaccination type: [partial, full]");
            return false;
        }
    }

    private void showAvailableOptions() {

        for (Integer integer:availableInquiries) {
            System.out.println(integer);

        }
    }

    private void endOutputMessage() {
        System.out.println("END OUTPUT\n");
    }

    private void beginOutputMessage() {

        System.out.println("\nBEGIN OUTPUT");
    }
}