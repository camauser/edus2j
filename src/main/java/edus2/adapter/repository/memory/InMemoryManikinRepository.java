package edus2.adapter.repository.memory;

import edus2.domain.Manikin;
import edus2.domain.ManikinRepository;

import java.util.*;

public class InMemoryManikinRepository implements ManikinRepository {

    private Map<String, Manikin> manikinMap;

    public InMemoryManikinRepository() {
        manikinMap = new HashMap<>();
    }
    @Override
    public Optional<Manikin> retrieve(String name) {
        return Optional.ofNullable(manikinMap.get(name));
    }

    @Override
    public List<Manikin> retrieveAll() {
        return new ArrayList<>(manikinMap.values());
    }

    @Override
    public void save(Manikin manikin) {
        manikinMap.put(manikin.getName(), manikin);
    }

    @Override
    public void remove(String name) {
        manikinMap.remove(name);
    }
}
