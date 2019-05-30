package util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author owen151128@gmail.com
 * <p>
 * Created by owen151128 on 2019-05-30 10:56
 * <p>
 * Providing features related to InputArgumentsParser class
 */
public class InputArgumentsParser {
    private static final Logger logger = Logger.getLogger(InputArgumentsParser.class.getName());
    private static InputArgumentsParser instance = null;

    private static final String PATH_MODE = "-p";
    private static final String INPUT_MODE = "-i";
    private static final String HELP_MODE = "--h";
    private static final String VERSION_MODE = "--v";

    private static final String COMMA = ",";

    private static final String ERR_ILLEGAL_OPTION = "This option does not exist : ";

    private static final String MSG_VERSION = "Apk Analyzer version : 0.1";
    private static final String MSG_USEAGE = "Usage : Apk Analyzer [options] <input-path> ..." +
            "\n" +
            "where <input-path> are any file possible!" +
            "\n" +
            "and options are :" +
            "\n" +
            "-p \t\t working directory path <input-path>" +
            "\n" +
            "-i \t\t input apk path <input-path>" +
            "\n" +
            "--h \t print this message." +
            "\n" +
            "--v \t print the version of Apk Analyzer";

    private HashSet<String> doubleOptionSet = null;
    private HashSet<String> singleOptionSet = null;
    private HashMap<String, String> valueMap = null;

    private String[] arguments;

    /**
     * Use Single-tone pattern
     *
     * @param args Application's Arguments
     * @return InputArgumentsParser's instance
     */
    public static synchronized InputArgumentsParser getInstance(String[] args) {
        if (instance == null) {
            instance = new InputArgumentsParser(args);
        }

        return instance;
    }

    /**
     * Private Default Costructor
     *
     * @param args Application's arguments array
     */
    private InputArgumentsParser(String[] args) {
        arguments = args;
        initialization();
    }

    /**
     * Input Arguments Parser initialization
     */
    private void initialization() {
        if (doubleOptionSet != null) {
            doubleOptionSet.clear();
        }

        if (valueMap != null) {
            valueMap.clear();
        }

        doubleOptionSet = new HashSet<>();
        singleOptionSet = new HashSet<>();
        valueMap = new HashMap<>();

        addOption();
        parser();
    }

    /**
     * Parse Input Arguments
     */
    private void parser() {
        boolean isValid = argumentsCheck(arguments);

        if (!isValid) {
            return;
        }

        String opt = null;
        String value;

        boolean isSingleOption = false;
        boolean isOptionEntered = false;

        for (String argument : arguments) {

            for (String singleOption : singleOptionSet) {
                if (argument.equals(singleOption)) {
                    isSingleOption = true;
                    opt = argument;
                    break;
                }
            }

            if (isSingleOption) {
                switch (opt) {
                    case HELP_MODE:
                        printUsage();
                        break;
                    case VERSION_MODE:
                        printVersion();
                }

                isSingleOption = false;

            } else {
                if (!isOptionEntered) {
                    opt = argument;

                    if (!isExistOption(opt)) {
                        logger.log(Level.SEVERE, ERR_ILLEGAL_OPTION + opt);

                        return;
                    }

                    isOptionEntered = true;

                } else {
                    value = argument;
                    addValueMap(opt, value);
                    isOptionEntered = false;
                }
            }
        }
    }

    /**
     * Add option into OptionSet
     */
    private void addOption() {
        doubleOptionSet.add(PATH_MODE);
        doubleOptionSet.add(INPUT_MODE);
        singleOptionSet.add(HELP_MODE);
        singleOptionSet.add(VERSION_MODE);
    }

    /**
     * Check input option exist optionSet
     *
     * @param option Input Option
     * @return If exist, return true
     */
    private boolean isExistOption(String option) {
        return doubleOptionSet.contains(option);
    }

    /**
     * Check input option exist valueMap
     *
     * @param option Input Option
     * @return If exist, return true
     */
    private boolean isExistValue(String option) {
        return valueMap.containsKey(option);
    }

    /**
     * Add into valueMap
     *
     * @param option Input Option
     * @param value  Input Value
     */
    private void addValueMap(String option, String value) {
        valueMap.put(option, value);
    }

    /**
     * Verify the Arguments are correct
     *
     * @param args Application's arguments
     * @return if correct, return true
     */
    private boolean argumentsCheck(String[] args) {
        int length = args.length;

        if (length < 1) {
            printUsage();

            return false;
        }

        return true;
    }

    /**
     * Check value not exist in valueMap
     *
     * @return If valueMap is empty, return true
     */
    private boolean isValueNotExistInValueMap() {
        if (valueMap == null) {
            return false;
        } else {
            return valueMap.isEmpty();
        }
    }

    /**
     * Get working directory path
     *
     * @return Working directory path
     */
    public Path getWorkingDirectoryPath() {
        if (isValueNotExistInValueMap()) {
            return null;
        }

        if (isExistValue(PATH_MODE)) {
            return Paths.get(valueMap.get(PATH_MODE));
        }

        return null;
    }

    /**
     * Get input apk path
     *
     * @return Input apk path
     */
    public Path[] getInputApkApth() {
        Path[] resultArray = null;

        if (isValueNotExistInValueMap()) {
            return null;
        }

        if (isExistValue(INPUT_MODE)) {

            if (valueMap.get(INPUT_MODE).contains(COMMA)) {
                String[] splitArgument = valueMap.get(INPUT_MODE).split(COMMA);

                resultArray = new Path[splitArgument.length];

                for (int i = 0; i < resultArray.length; i++) {
                    resultArray[i] = Paths.get(splitArgument[i]);
                }

                return resultArray;
            } else {
                resultArray = new Path[1];
                resultArray[0] = Paths.get(valueMap.get(INPUT_MODE));

                return resultArray;
            }
        }

        return null;
    }

    /**
     * Print usage Method
     */
    private void printUsage() {
        System.out.println(MSG_USEAGE);
    }

    /**
     * Print version Method
     */
    private void printVersion() {
        System.out.println(MSG_VERSION);
    }
}
