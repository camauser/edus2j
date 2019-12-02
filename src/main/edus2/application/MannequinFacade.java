package edus2.application;

import edus2.domain.DuplicateScanTagException;
import edus2.domain.InvalidMannequinNameException;
import edus2.domain.Mannequin;
import edus2.domain.MannequinRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
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

    public Optional<Mannequin> getMannequin(String name) {
        return mannequinRepository.retrieve(name);
    }

    public List<Mannequin> getAllMannequins() {
        return mannequinRepository.retrieveAll();
    }

    public void save(Mannequin mannequin) {
        validateUniqueScanTags(mannequin);
        mannequinRepository.save(mannequin);
    }

    public void rename(String currentName, String newName) {
        Optional<Mannequin> currentMannequin = mannequinRepository.retrieve(currentName);
        Optional<Mannequin> destinationMannequinName = mannequinRepository.retrieve(newName);
        if (!currentMannequin.isPresent()) {
            throw new InvalidMannequinNameException(String.format("Mannequin '%s' does not exist!", currentName));
        }

        if (destinationMannequinName.isPresent()) {
            throw new InvalidMannequinNameException(String.format("Mannequin '%s' already exists, '%s' cannot be renamed to '%s'", newName, currentName, newName));
        }

        Mannequin newMannequin = new Mannequin(currentMannequin.get().getTagMap(), newName);
        mannequinRepository.remove(currentName);
        mannequinRepository.save(newMannequin);
    }

    public void remove(Mannequin mannequin) {
        mannequinRepository.remove(mannequin.getName());
    }

    private Set<MannequinScanTagIdentifier> getAllScanTags() {

        return getAllMannequins()
                .stream()
                .flatMap(m -> m.getTagMap().values().stream().map(t -> new MannequinScanTagIdentifier(m.getName(), t)))
                .collect(Collectors.toSet());
    }

    private void validateUniqueScanTags(Mannequin mannequin) {
        Set<MannequinScanTagIdentifier> otherMannequinScanTags = getAllScanTags()
                .stream()
                .filter(st -> !st.getMannequinName().equals(mannequin.getName()))
                .collect(Collectors.toSet());
        for (String scanTag : mannequin.getTagMap().values()) {
            Optional<MannequinScanTagIdentifier> existingIdentifierOptional = otherMannequinScanTags
                    .stream()
                    .filter(ms -> ms.getScanTag().equals(scanTag))
                    .findFirst();
            if (existingIdentifierOptional.isPresent()) {
                MannequinScanTagIdentifier identifier = existingIdentifierOptional.get();
                throw new DuplicateScanTagException(String.format("Scan tag '%s' already exists on mannequin '%s'",
                        identifier.getScanTag(), identifier.getMannequinName()));
            }
        }
    }

    private class MannequinScanTagIdentifier {
        private final String mannequinName;
        private final String scanTag;

        public MannequinScanTagIdentifier(String mannequinName, String scanTag) {
            this.mannequinName = mannequinName;
            this.scanTag = scanTag;
        }

        public String getMannequinName() {
            return mannequinName;
        }

        public String getScanTag() {
            return scanTag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            MannequinScanTagIdentifier that = (MannequinScanTagIdentifier) o;

            return new EqualsBuilder()
                    .append(mannequinName, that.mannequinName)
                    .append(scanTag, that.scanTag)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(mannequinName)
                    .append(scanTag)
                    .toHashCode();
        }
    }
}
