package edus2.application;

import edus2.domain.Mannequin;
import edus2.domain.MannequinRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MannequinFacade {
    private MannequinRepository mannequinRepository;

    @Inject
    public MannequinFacade(MannequinRepository mannequinRepository) {
        this.mannequinRepository = mannequinRepository;
    }

    public Set<String> getMannequinNames() {
        return mannequinRepository.retrieveAll().stream().map(Mannequin::getName).collect(Collectors.toSet());
    }

    public List<Mannequin> getAllMannequins() {
        return mannequinRepository.retrieveAll();
    }
}
