package edus2.adapter.guice.provider;

import com.google.inject.Provider;
import edus2.adapter.repository.file.FileEDUS2Configuration;
import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileEDUS2ConfigurationProvider implements Provider<FileEDUS2Configuration> {
    private static final String CONFIG_FILE_NAME = "edus2j.conf";
    private static final String CONFIG_FILE_DIRECTORY_NAME = "edus2j";

    @Override
    public FileEDUS2Configuration get() {
        Path configDirectory;
        if (SystemUtils.IS_OS_WINDOWS) {
            configDirectory = Paths.get(System.getenv("APPDATA"), CONFIG_FILE_DIRECTORY_NAME);
        } else {
            configDirectory = Paths.get(System.getProperty("user.home"), CONFIG_FILE_DIRECTORY_NAME);
        }

        Path configFilePath = configDirectory.resolve(CONFIG_FILE_NAME);
        createDirectory(configFilePath.getParent());
        return new FileEDUS2Configuration(configFilePath.toString());
    }

    private void createDirectory(Path saveFileDirectory) {
        try {
            Files.createDirectories(saveFileDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
