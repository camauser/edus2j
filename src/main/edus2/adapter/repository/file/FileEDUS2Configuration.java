package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edus2.domain.EDUS2Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileEDUS2Configuration extends FileRepository implements EDUS2Configuration {

    public static final String CONFIG_FILE_NAME = "edus2j.conf";
    private Gson gson;

    public FileEDUS2Configuration(String saveFileDirectory) {
        super(saveFileDirectory + "\\" + CONFIG_FILE_NAME);
        Path path = Paths.get(saveFileDirectory);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gson = new GsonBuilder().create();
    }

    public FileEDUS2Configuration(String fileDirectory, String fileName) {
        super(fileDirectory + "\\" + fileName);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public Optional<Integer> getMinimumVideoHeight() {
        return getDto().map(dto -> dto.minimumVideoHeight);
    }

    @Override
    public Optional<Integer> getMinimumVideoWidth() {
        return getDto().map(dto -> dto.minimumVideoWidth);
    }

    @Override
    public Optional<String> getHashedPassword() {
        return getDto().map(dto -> dto.hashedPassword);
    }

    @Override
    public Optional<String> getScanFileLocation() {
        return getDto().map(dto -> dto.scanFileLocation);
    }

    @Override
    public void setMinimumVideoHeight(int minimumVideoHeight) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.minimumVideoHeight = minimumVideoHeight;
        saveToFile(gson.toJson(dto));
    }

    @Override
    public void setMinimumVideoWidth(int minimumVideoWidth) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.minimumVideoWidth = minimumVideoWidth;
        saveToFile(gson.toJson(dto));
    }

    @Override
    public void setHashedPassword(String hashedPassword) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.hashedPassword = hashedPassword;
        saveToFile(gson.toJson(dto));
    }

    @Override
    public void setScanFileLocation(String scanFileLocation) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.scanFileLocation = scanFileLocation;
        saveToFile(gson.toJson(dto));
    }

    private Optional<EDUS2ConfigurationDto> getDto() {
        Optional<String> fileContents = readFileContents();
        return fileContents.map(s -> gson.fromJson(s, EDUS2ConfigurationDto.class));
    }

    private class EDUS2ConfigurationDto {
        protected Integer minimumVideoHeight;
        protected Integer minimumVideoWidth;
        protected String hashedPassword;
        protected String scanFileLocation;
    }
}
