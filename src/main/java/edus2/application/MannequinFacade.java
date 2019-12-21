package edus2.application;

import edus2.domain.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.inject.Inject;
import java.util.*;
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

    public void create(Mannequin mannequin) {
        validateUniqueScanTags(mannequin);
        validateUniqueName(mannequin);
        mannequinRepository.save(mannequin);
    }

    private void validateUniqueName(Mannequin mannequin) {
        if (nameExists(mannequin.getName())) {
            throw new InvalidMannequinNameException(String.format("A mannequin named %s already exists!", mannequin.getName()));
        }
    }

    public void update(Mannequin mannequin) {
        validateMannequinExists(mannequin);
        validateUniqueScanTags(mannequin);
        mannequinRepository.save(mannequin);
    }

    private void validateMannequinExists(Mannequin mannequin) {
        if (!nameExists(mannequin.getName())) {
            throw new InvalidMannequinNameException(String.format("A mannequin named %s does not exist!", mannequin.getName()));
        }
    }

    private boolean nameExists(String name) {
        return getAllMannequins().stream().anyMatch(m -> m.getName().equals(name));
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

    public Optional<MannequinScanEnum> getScanTagLocation(String scan) {
        return getAllScanTags()
                .stream()
                .filter(st -> st.getScanTag().equals(scan))
                .map(MannequinScanTagIdentifier::getScanLocation)
                .findFirst();
    }

    private Set<MannequinScanTagIdentifier> getAllScanTags() {
        Set<MannequinScanTagIdentifier> scanTagIdentifiers = new HashSet<>();
        for (Mannequin mannequin : getAllMannequins()) {
            for (Map.Entry<MannequinScanEnum, String> scan : mannequin.getTagMap().entrySet()) {
                scanTagIdentifiers.add(new MannequinScanTagIdentifier(mannequin.getName(), scan.getValue(), scan.getKey()));
            }
        }

        return scanTagIdentifiers;
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
        private final MannequinScanEnum scanLocation;

        public MannequinScanTagIdentifier(String mannequinName, String scanTag, MannequinScanEnum scanLocation) {
            this.mannequinName = mannequinName;
            this.scanTag = scanTag;
            this.scanLocation = scanLocation;
        }

        public String getMannequinName() {
            return mannequinName;
        }

        public String getScanTag() {
            return scanTag;
        }

        public MannequinScanEnum getScanLocation() {
            return scanLocation;
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