package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edus2.domain.EDUS2Configuration;

import java.util.Optional;

public abstract class FileCentralRepository extends FileRepository {
    private Gson gson;
    private EDUS2Configuration configuration;
    private static final String DEFAULT_SAVE_FILE = "EDUS2Data.json";

    public FileCentralRepository(EDUS2Configuration configuration) {
        this.configuration = configuration;
        this.gson = new GsonBuilder().create();
    }

    protected Optional<String> retrieveSection() {
        Optional<String> fileContentsOptional = readFileContents(getFileLocation());
        if (!fileContentsOptional.isPresent()) {
            return Optional.empty();
        }

        JsonObject json = gson.fromJson(fileContentsOptional.get(), JsonObject.class);
        JsonElement section = json.get(getSectionName());
        if (section == null) {
            return Optional.empty();
        }

        return Optional.of(section.getAsString());
    }

    protected void saveSection(String json) {
        Optional<String> fileContentsOptional = readFileContents(getFileLocation());
        JsonObject fileJson;
        if (fileContentsOptional.isPresent()) {
            fileJson = gson.fromJson(fileContentsOptional.get(), JsonObject.class);
        } else {
            fileJson = new JsonObject();
        }
        fileJson.addProperty(getSectionName(), json);
        saveToFile(gson.toJson(fileJson), getFileLocation());
    }

    private String getFileLocation() {
        return configuration.getSaveFileLocation().orElse(DEFAULT_SAVE_FILE);
    }

    protected abstract String getSectionName();
}
