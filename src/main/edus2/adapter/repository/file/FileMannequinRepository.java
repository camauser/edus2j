package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.domain.Mannequin;
import edus2.domain.MannequinRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileMannequinRepository extends FileRepository implements MannequinRepository {

    private String filePath;
    private final Gson gson;

    public FileMannequinRepository(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public Optional<Mannequin> retrieve(String name) {
        return retrieveAll().stream().filter(m -> m.getName().equals(name)).findFirst();
    }

    @Override
    public List<Mannequin> retrieveAll() {
        Type type = new TypeToken<List<Mannequin>>(){}.getType();
        Optional<String> fileContents = readFileContents(filePath);
        //noinspection unchecked
        return fileContents.map(fc -> (List<Mannequin>)gson.fromJson(fc, type)).orElse(new ArrayList<>());
    }

    @Override
    public void save(Mannequin mannequin) {
        List<Mannequin> mannequins = retrieveAll();
        mannequins.removeIf(m -> m.getName().equals(mannequin.getName()));
        mannequins.add(mannequin);
        saveToFile(gson.toJson(mannequins), filePath);
    }

    @Override
    public void remove(String name) {
        List<Mannequin> mannequins = retrieveAll();
        mannequins.removeIf(m -> m.getName().equals(name));
        saveToFile(gson.toJson(mannequins), filePath);
    }
}
