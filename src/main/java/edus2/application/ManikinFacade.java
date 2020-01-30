package edus2.application;

import edus2.domain.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class ManikinFacade {
    private ManikinRepository manikinRepository;

    @Inject
    public ManikinFacade(ManikinRepository manikinRepository) {
        this.manikinRepository = manikinRepository;
    }

    public Optional<Manikin> getManikin(String name) {
        return manikinRepository.retrieve(name);
    }

    public List<Manikin> getAllManikins() {
        return manikinRepository.retrieveAll();
    }

    public void create(Manikin manikin) {
        validateUniqueScanTags(manikin);
        validateUniqueName(manikin);
        manikinRepository.save(manikin);
    }

    private void validateUniqueName(Manikin manikin) {
        if (nameExists(manikin.getName())) {
            throw new InvalidManikinNameException(String.format("A manikin named %s already exists!", manikin.getName()));
        }
    }

    public void update(Manikin manikin) {
        validateManikinExists(manikin);
        validateUniqueScanTags(manikin);
        manikinRepository.save(manikin);
    }

    private void validateManikinExists(Manikin manikin) {
        if (!nameExists(manikin.getName())) {
            throw new InvalidManikinNameException(String.format("A manikin named %s does not exist!", manikin.getName()));
        }
    }

    private boolean nameExists(String name) {
        return getAllManikins().stream().anyMatch(m -> m.getName().equals(name));
    }

    public void rename(String currentName, String newName) {
        Optional<Manikin> currentManikin = manikinRepository.retrieve(currentName);
        Optional<Manikin> destinationManikinName = manikinRepository.retrieve(newName);
        if (!currentManikin.isPresent()) {
            throw new InvalidManikinNameException(String.format("Manikin '%s' does not exist!", currentName));
        }

        if (destinationManikinName.isPresent()) {
            throw new InvalidManikinNameException(String.format("Manikin '%s' already exists, '%s' cannot be renamed to '%s'", newName, currentName, newName));
        }

        Manikin newManikin = new Manikin(currentManikin.get().getTagMap(), newName);
        manikinRepository.remove(currentName);
        manikinRepository.save(newManikin);
    }

    public void remove(Manikin manikin) {
        manikinRepository.remove(manikin.getName());
    }

    public Optional<ManikinScanEnum> getScanTagLocation(String scan) {
        return getAllScanTags()
                .stream()
                .filter(st -> st.getScanTag().equals(scan))
                .map(ManikinScanTagIdentifier::getScanLocation)
                .findFirst();
    }

    private Set<ManikinScanTagIdentifier> getAllScanTags() {
        Set<ManikinScanTagIdentifier> scanTagIdentifiers = new HashSet<>();
        for (Manikin manikin : getAllManikins()) {
            for (Map.Entry<ManikinScanEnum, String> scan : manikin.getTagMap().entrySet()) {
                scanTagIdentifiers.add(new ManikinScanTagIdentifier(manikin.getName(), scan.getValue(), scan.getKey()));
            }
        }

        return scanTagIdentifiers;
    }

    private void validateUniqueScanTags(Manikin manikin) {
        Set<ManikinScanTagIdentifier> otherManikinScanTags = getAllScanTags()
                .stream()
                .filter(st -> !st.getManikinName().equals(manikin.getName()))
                .collect(Collectors.toSet());
        for (String scanTag : manikin.getTagMap().values()) {
            Optional<ManikinScanTagIdentifier> existingIdentifierOptional = otherManikinScanTags
                    .stream()
                    .filter(ms -> ms.getScanTag().equals(scanTag))
                    .findFirst();
            if (existingIdentifierOptional.isPresent()) {
                ManikinScanTagIdentifier identifier = existingIdentifierOptional.get();
                throw new DuplicateScanTagException(String.format("Scan tag '%s' already exists on manikin '%s'",
                        identifier.getScanTag(), identifier.getManikinName()));
            }
        }
    }

    private static class ManikinScanTagIdentifier {
        private final String manikinName;
        private final String scanTag;
        private final ManikinScanEnum scanLocation;

        ManikinScanTagIdentifier(String manikinName, String scanTag, ManikinScanEnum scanLocation) {
            this.manikinName = manikinName;
            this.scanTag = scanTag;
            this.scanLocation = scanLocation;
        }

        String getManikinName() {
            return manikinName;
        }

        String getScanTag() {
            return scanTag;
        }

        ManikinScanEnum getScanLocation() {
            return scanLocation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            ManikinScanTagIdentifier that = (ManikinScanTagIdentifier) o;

            return new EqualsBuilder()
                    .append(manikinName, that.manikinName)
                    .append(scanTag, that.scanTag)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(manikinName)
                    .append(scanTag)
                    .toHashCode();
        }
    }
}
