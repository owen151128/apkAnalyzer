package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author owen151128@gmail.com
 * <p>
 * Created by owen151128 on 2019-05-30 11:37
 * <p>
 * Providing features related to ZipUtil class
 */
public class ZipUtil {
    private static final int BUFFER_SIZE = 1024;

    /**
     * Un zip Method
     *
     * @param zipFilePath  Un zip target's path
     * @param unZipDirPath Un zip working directory path
     * @return if success, return true
     */
    public static boolean unZip(Path zipFilePath, Path unZipDirPath) {
        byte[] buffer = new byte[BUFFER_SIZE];

        ZipInputStream zipInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            if (Files.notExists(unZipDirPath)) {
                Files.createDirectories(unZipDirPath);
            }

            zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath.toFile()));

            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                Path entryFilePath = Paths.get(unZipDirPath.toAbsolutePath().toString() + File.separator + zipEntry.getName());

                if (Files.notExists(entryFilePath.getParent())) {
                    Files.createDirectories(entryFilePath.getParent());
                }

                fileOutputStream = new FileOutputStream(entryFilePath.toFile());

                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.closeEntry();
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
