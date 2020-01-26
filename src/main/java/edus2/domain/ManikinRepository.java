package edus2.domain;

import java.util.List;
import java.util.Optional;

public interface ManikinRepository {
    Optional<Manikin> retrieve(String name);

    List<Manikin> retrieveAll();

    void save(Manikin manikin);

    void remove(String name);
}
