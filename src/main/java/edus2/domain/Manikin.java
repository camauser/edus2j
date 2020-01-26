package edus2.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Manikin {
    private final String name;
    private final Map<ManikinScanEnum, String> tagMap;

    public Manikin(Map<ManikinScanEnum, String> tagMap, String name) {
        validateName(name);
        validateAllScanPointsPresent(tagMap);
        this.name = name;
        this.tagMap = tagMap;
    }

    public String getName() {
        return name;
    }

    public Map<ManikinScanEnum, String> getTagMap() {
        return tagMap;
    }

    private void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidManikinNameException("Empty manikin name given.");
        }
    }

    private void validateAllScanPointsPresent(Map<ManikinScanEnum, String> tagMap) {
        for (ManikinScanEnum scanPoint : ManikinScanEnum.values()) {
            if (!tagMap.containsKey(scanPoint)) {
                throw new MissingRequiredScanPointException(String.format("Missing entry for %s", scanPoint.getName()));
            }
        }

        Set<String> scans = new HashSet<>();
        for (String scan : tagMap.values()) {
            if (StringUtils.isEmpty(scan)) {
                throw new MissingRequiredScanPointException("One or more locations are missing a scan tag.");
            }
            if (scans.contains(scan)) {
                throw new DuplicateScanTagException(String.format("Scan tag %s was listed more than once", scan));
            }
            scans.add(scan);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Manikin manikin = (Manikin) o;

        return new EqualsBuilder()
                .append(name, manikin.name)
                .append(tagMap, manikin.tagMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(tagMap)
                .toHashCode();
    }
}
