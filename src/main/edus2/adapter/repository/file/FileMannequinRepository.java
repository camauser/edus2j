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

public class FileMannequinRepository extends FileCentralRepository implements MannequinRepository {

    private static final String MANNEQUIN_SECTION_NAME = "mannequins";
    private static final Type MANNEQUIN_SECTION_TYPE = new TypeToken<List<Mannequin>>(){}.getType();
    private final Gson gson;

    public FileMannequinRepository(String filePath) {
        super(filePath);
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected String getSectionName() {
        return MANNEQUIN_SECTION_NAME;
    }

    @Override
    public Optional<Mannequin> retrieve(String name) {
        return retrieveAll().stream().filter(m -> m.getName().equals(name)).findFirst();
    }

    @Override
    public List<Mannequin> retrieveAll() {
        Optional<String> json = retrieveSection();
        //noinspection unchecked
        return json.map(fc -> (List<Mannequin>)gson.fromJson(fc, MANNEQUIN_SECTION_TYPE)).orElse(new ArrayList<>());
    }

    @Override
    public void save(Mannequin mannequin) {
        List<Mannequin> mannequins = retrieveAll();
        mannequins.removeIf(m -> m.getName().equals(mannequin.getName()));
        mannequins.add(mannequin);
        saveSection(gson.toJson(mannequins));
    }

    @Override
    public void remove(String name) {
        List<Mannequin> mannequins = retrieveAll();
        mannequins.removeIf(m -> m.getName().equals(name));
        saveSection(gson.toJson(mannequins));
    }
}
