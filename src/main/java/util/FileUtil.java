package util;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author owen151128@gmail.com
 * <p>
 * Created by owen151128 on 2019-05-30 12:00
 * <p>
 * Providing features related to FileUtil class
 */
public class FileUtil {

    /**
     * Get all file from target directory
     *
     * @param dirPath      Target directory's path
     * @param filePathList file's arrayList
     */
    public static void getAllFilesFromDir(Path dirPath, ArrayList<Path> filePathList) {
        File[] fileArray = dirPath.toFile().listFiles();
        File tmpFile = null;

        if (fileArray != null) {
            for (File f : fileArray) {
                if (f.isDirectory()) {
                    getAllFilesFromDir(f.toPath(), filePathList);
                } else {
                    if (f.getName().equals(".DS_Store")) {
                        continue;
                    }
                    tmpFile = new File(f.getAbsolutePath());
                    filePathList.add(tmpFile.toPath());
                }
            }
        }
    }
}
