package store.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import store.enums.ErrorMessage;

public class FileParser {

    public List<String> readAllFiles(final String filePath) {
        try {
            List<String> fileLines = Files.readAllLines(Paths.get(filePath));
            return fileLines;
        } catch (IOException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_FILE_FORMAT.getMessage());
        }
    }
}
