package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edus2.domain.EDUS2Configuration;

import java.nio.file.Paths;
import java.util.Optional;

public class FileEDUS2Configuration extends FileRepository implements EDUS2Configuration {

    private Gson gson;
    private String filePath;

    public FileEDUS2Configuration(String saveFilePath) {
        this.filePath = Paths.get(saveFilePath).toString();
        this.gson = new GsonBuilder().create();
    }

    FileEDUS2Configuration(String fileDirectory, String fileName) {
        this.filePath = fileDirectory + "\\" + fileName;
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
    public Optional<String> getSaveFileLocation() {
        return getDto().map(dto -> dto.saveFileLocation);
    }

    @Override
    public void setMinimumVideoHeight(int minimumVideoHeight) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.minimumVideoHeight = minimumVideoHeight;
        saveToFile(gson.toJson(dto), filePath);
    }

    @Override
    public void setMinimumVideoWidth(int minimumVideoWidth) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.minimumVideoWidth = minimumVideoWidth;
        saveToFile(gson.toJson(dto), filePath);
    }

    @Override
    public void setHashedPassword(String hashedPassword) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.hashedPassword = hashedPassword;
        saveToFile(gson.toJson(dto), filePath);
    }

    @Override
    public void setSaveFileLocation(String saveFileLocation) {
        Optional<EDUS2ConfigurationDto> dtoOptional = getDto();
        EDUS2ConfigurationDto dto = dtoOptional.orElseGet(EDUS2ConfigurationDto::new);
        dto.saveFileLocation = saveFileLocation;
        saveToFile(gson.toJson(dto), filePath);
    }

    private Optional<EDUS2ConfigurationDto> getDto() {
        Optional<String> fileContents = readFileContents(filePath);
        return fileContents.map(s -> gson.fromJson(s, EDUS2ConfigurationDto.class));
    }

    private class EDUS2ConfigurationDto {
        protected Integer minimumVideoHeight;
        protected Integer minimumVideoWidth;
        protected String hashedPassword;
        protected String saveFileLocation;
    }
}
