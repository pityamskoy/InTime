package team.capybara.backend.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileFromStorageStore {
    private static final Logger log = LoggerFactory.getLogger(FileFromStorageStore.class);

    public void saveFile(String path, String fileName, byte[] values) throws IOException {
        Path directoryPath = Paths.get(path);
        Path filePath = directoryPath.resolve(fileName);
        Files.write(filePath, values, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public byte[] readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        return Files.readAllBytes(filePath);

    }

}
