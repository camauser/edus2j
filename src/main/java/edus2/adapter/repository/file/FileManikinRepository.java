package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import edus2.domain.EDUS2Configuration;
import edus2.domain.Manikin;
import edus2.domain.ManikinRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileManikinRepository extends FileCentralRepository implements ManikinRepository {

    private static final String MANIKIN_SECTION_NAME = "mannequins";
    private static final Type MANIKIN_SECTION_TYPE = new TypeToken<List<Manikin>>(){}.getType();
    private final Gson gson;

    @Inject
    public FileManikinRepository(EDUS2Configuration configuration) {
        super(configuration);
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected String getSectionName() {
        return MANIKIN_SECTION_NAME;
    }

    @Override
    public Optional<Manikin> retrieve(String name) {
        return retrieveAll().stream().filter(m -> m.getName().equals(name)).findFirst();
    }

    @Override
    public List<Manikin> retrieveAll() {
        Optional<String> json = retrieveSection();
        //noinspection unchecked
        return json.map(fc -> (List<Manikin>)gson.fromJson(fc, MANIKIN_SECTION_TYPE)).orElse(new ArrayList<>());
    }

    @Override
    public void save(Manikin manikin) {
        List<Manikin> manikins = retrieveAll();
        manikins.removeIf(m -> m.getName().equals(manikin.getName()));
        manikins.add(manikin);
        saveSection(gson.toJson(manikins));
    }

    @Override
    public void remove(String name) {
        List<Manikin> manikins = retrieveAll();
        manikins.removeIf(m -> m.getName().equals(name));
        saveSection(gson.toJson(manikins));
    }
}
