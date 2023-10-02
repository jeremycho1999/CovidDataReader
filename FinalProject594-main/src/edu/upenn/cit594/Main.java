package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.DataManager;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.DataProcessor;
import edu.upenn.cit594.ui.ErrorDisplayer;
import edu.upenn.cit594.ui.Interacter;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    //names of file values (actual name of the particular file)
    private static String covidFileValue=null;
    private static String populationFileValue=null;
    private static String propertyFileValue=null;
    private static String logFileValue=null;

    //name of possible file names(description of file context) to ensure argument file names match these
    private static final String COVID_FILE_NAME="covid";
    private static final String POPULATION_FILE_NAME ="population";
    private static final String PROPERTY_FILE_NAME="properties";
    private static final String LOG_FILE_NAME="log";

    private static final Set<String> setOfPossibleFileNames = new HashSet<>(Arrays.asList
            (COVID_FILE_NAME,POPULATION_FILE_NAME,PROPERTY_FILE_NAME,LOG_FILE_NAME));

    public static void main(String[] args) throws IOException {

        //ensure the syntax and structure of the given arguments are correct
        makeSureGivenFileNamesAreValid(args);

        //ensure all input files except the log file exists and can be opened
        makeSureFilesExistAndCanBeOpened();

        //Set up Logger
        Logger logger= Logger.getInstance();
        Logger.setDestination(logFileValue);//MUST CHECK IF LOG FILE CAN BE INSTANTIATED
        logger.writeCMDLineArgs(args);

        //Get Data
        DataManager dataManager= new DataManager(covidFileValue,populationFileValue,propertyFileValue);

        //Process Data
        DataProcessor dataProcessor= new DataProcessor(dataManager);

        //Allow user to interact with Data
        Interacter interacter= new Interacter(dataProcessor);
        interacter.start();

    }

    /**
     * checks the given file names are valid based on valid argument structure
     *
     * @param args to check
     */
    private static void makeSureGivenFileNamesAreValid(String[] args) {

        //make sure argument matches "--name=value" structure
        String fileRegex= "^--(?<name>.+?)=(?<value>.+)$";
        Pattern pattern= Pattern.compile(fileRegex);

        String nameOfFile;
        String valueOfFile;

        //make sure name of argument is not used more than once
        Set<String> previousNames= new HashSet<>();

        Matcher matcher;

        for (String givenArgument:args) {

            matcher=pattern.matcher(givenArgument);
            if (!matcher.find()) {
                ErrorDisplayer.displayInputSyntaxError("Given argument does not match '--name=value' structure");
            }

            nameOfFile=matcher.group(1);
            if (!setOfPossibleFileNames.contains(nameOfFile))
                ErrorDisplayer.displayInputSyntaxError(String.format("Name of given argument %s must of the following list:" +
                        " ['covid','log','properties','population']",nameOfFile));

            if (previousNames.contains(nameOfFile))
                ErrorDisplayer.displayInputSyntaxError(String.format("Name of given argument %s may only be given once ",
                        nameOfFile));
            previousNames.add(nameOfFile);


            valueOfFile= matcher.group(2);
            if (COVID_FILE_NAME.equals(nameOfFile)) {
                checkIfFileNameIsCSVOrJSON(valueOfFile);
                covidFileValue = "src/"+ valueOfFile;
            }
            else if (POPULATION_FILE_NAME.equals(nameOfFile)) {
                checkIfFileNameIsCSV(valueOfFile);
                populationFileValue ="src/"+ valueOfFile;
            }
            else if (PROPERTY_FILE_NAME.equals(nameOfFile)) {
                checkIfFileNameIsCSV(valueOfFile);
                propertyFileValue ="src/"+ valueOfFile;

            } else if (LOG_FILE_NAME.equals(nameOfFile)) {
                logFileValue ="src/"+ valueOfFile;
            }
        }
    }

    /**
     * Check if a given file name is a csv
     *
     * @param file_name to check
     */
    private static void checkIfFileNameIsCSV(String file_name) {

        if (!file_name.toLowerCase().endsWith(".csv"))
            ErrorDisplayer.displayInputSyntaxError("Given argument file name must end with '.csv' ");
    }

    private static void checkIfFileNameIsCSVOrJSON(String file_name) {

        if (!file_name.toLowerCase().endsWith(".csv") && !file_name.toLowerCase().endsWith(".json"))
            ErrorDisplayer.displayInputSyntaxError("Given argument file name must end with '.csv' or '.json' ");
    }

    /**
     * Checks that files exist and can can be opened
     *
     * @throws NoSuchFileException
     */
    private static void makeSureFilesExistAndCanBeOpened() throws NoSuchFileException {

        List<File> fileList= new ArrayList<>();

        if (covidFileValue!=null) {
            File covidFile= new File(covidFileValue);
            fileList.add(covidFile);
        }
        if (populationFileValue!=null) {
            File populationFile= new File(populationFileValue);
            fileList.add(populationFile);
        }
        if (propertyFileValue!=null) {
            File propertyFile= new File(propertyFileValue);
            fileList.add(propertyFile);
        }

        for (File file:fileList) {
            if (!file.exists())
                ErrorDisplayer.displayFileExistingError(file.getName());
            if (!file.canRead())
                ErrorDisplayer.displayFileOpeningError(file.getName());
        }
    }

}