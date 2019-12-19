package edus2.domain;

import java.util.List;
import java.util.Optional;

public interface MannequinRepository {
    Optional<Mannequin> retrieve(String name);

    List<Mannequin> retrieveAll();

    void save(Mannequin mannequin);

    void remove(String name);
}
