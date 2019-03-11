package pl.slovvik.logprocessor.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileHelper {

    public static final String NEW_LINE = System.getProperty("line.separator");

    private FileHelper() {

    }

    public static File creatFile(String filePath) {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                log.info("File {} created", filePath);
            }
        } catch (IOException e) {
            log.error("File {} not created, caused by {}", filePath, e);
        }
        return file;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            log.info("File {} deleted", filePath);
        } else {
            log.error("File {} not exists", filePath);
        }
    }
}
