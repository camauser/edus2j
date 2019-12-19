package edus2.adapter.repository.memory;

import edus2.domain.Mannequin;
import edus2.domain.MannequinRepository;

import java.util.*;

public class InMemoryMannequinRepository implements MannequinRepository {

    private Map<String, Mannequin> mannequinMap;

    public InMemoryMannequinRepository() {
        mannequinMap = new HashMap<>();
    }
    @Override
    public Optional<Mannequin> retrieve(String name) {
        return Optional.ofNullable(mannequinMap.get(name));
    }

    @Override
    public List<Mannequin> retrieveAll() {
        return new ArrayList<>(mannequinMap.values());
    }

    @Override
    public void save(Mannequin mannequin) {
        mannequinMap.put(mannequin.getName(), mannequin);
    }

    @Override
    public void remove(String name) {
        mannequinMap.remove(name);
    }
}
