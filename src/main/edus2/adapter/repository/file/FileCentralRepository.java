package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public abstract class FileCentralRepository extends FileRepository {
    private String fileName;
    private Gson gson;

    // TODO: figure out how to allow for save file location changes
    public FileCentralRepository(String fileName) {
        this.fileName = fileName;
        this.gson = new GsonBuilder().create();
    }

    protected Optional<String> retrieveSection() {
        Optional<String> fileContentsOptional = readFileContents(fileName);
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
        Optional<String> fileContentsOptional = readFileContents(fileName);
        JsonObject fileJson;
        if (fileContentsOptional.isPresent()) {
            fileJson = gson.fromJson(fileContentsOptional.get(), JsonObject.class);
        } else {
            fileJson = new JsonObject();
        }
        fileJson.addProperty(getSectionName(), json);
        saveToFile(gson.toJson(fileJson), fileName);
    }

    protected abstract String getSectionName();
}
