import util.Console;
import util.FileUtil;
import util.InputArgumentsParser;
import util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author owen151128@gmail.com
 * <p>
 * Created by owen151128 on 2019-05-30 10:38
 * <p>
 * Providing features related to Application class
 */
public class Application {
    private static final String APP_NAME = "APK Analyzer v1.0";
    private static final String LIB = "lib";
    private static final String MSG_SO_COUNT = "'s So count : ";
    private static final String MSG_DELETE_WORKING_DIR = "workingDIrectory was deleted!";
    private static final String MSG_DONE = "APK Analyzer Done!";

    /**
     * Application's Main method
     *
     * @param args Application's Arguments
     */
    public static void main(String[] args) {
        Console.bold(APP_NAME);

        InputArgumentsParser argumentsParser = InputArgumentsParser.getInstance(args);  //  initialize Input Arguments parser

        // get Path from Arguments
        Path workingDirParh = argumentsParser.getWorkingDirectoryPath();
        Path dirPath = argumentsParser.getInputDirPath();
        Path[] apkPath = argumentsParser.getInputApkApth();

        // Null check
        if (workingDirParh == null || (apkPath == null && dirPath == null) || (apkPath != null && dirPath != null)) {
            if (!argumentsParser.isPrintedUsage()) {
                InputArgumentsParser.printUsage();
            }

            return;
        }

        ArrayList<Path> soPathList = new ArrayList<>();

        if (apkPath != null) {
            for (Path apk : apkPath) {
                soPathList.clear();
                ZipUtil.unZip(apk, workingDirParh); //  unzip apk
                FileUtil.getAllFilesFromDir(Paths.get(workingDirParh.toAbsolutePath().toString() + File.separator + LIB), soPathList);  //  get So file List

                Console.write(apk.getFileName().toString() + MSG_SO_COUNT + soPathList.size()); //  print So File count

                try {
                    if (Files.exists(workingDirParh)) {
                        //  delete unzip apk files
                        Files.walk(workingDirParh)
                                .map(Path::toFile)
                                .sorted(Comparator.reverseOrder())
                                .forEach(File::delete);

                        System.out.println(MSG_DELETE_WORKING_DIR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (dirPath != null) {
            ArrayList<Path> apkList = new ArrayList<>();
            FileUtil.getAllFilesFromDir(dirPath, apkList);

            for (Path apk : apkList) {
                soPathList.clear();
                ZipUtil.unZip(apk, workingDirParh); //  unzip apk
                FileUtil.getAllFilesFromDir(Paths.get(workingDirParh.toAbsolutePath().toString() + File.separator + LIB), soPathList); // get So file List

                Console.write(apk.getFileName().toString() + MSG_SO_COUNT + soPathList.size()); //  print So File count

                try {
                    if (Files.exists(workingDirParh)) {
                        // delete unzip apk files
                        Files.walk(workingDirParh)
                                .map(Path::toFile)
                                .sorted(Comparator.reverseOrder())
                                .forEach(File::delete);

                        System.out.println(MSG_DELETE_WORKING_DIR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Console.bold(MSG_DONE);
    }
}
