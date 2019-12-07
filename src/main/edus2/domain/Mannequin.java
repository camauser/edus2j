package edus2.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Mannequin {
    private final String name;
    private final Map<MannequinScanEnum, String> tagMap;

    public Mannequin(Map<MannequinScanEnum, String> tagMap, String name) {
        validateName(name);
        validateAllScanPointsPresent(tagMap);
        this.name = name;
        this.tagMap = tagMap;
    }

    public String getName() {
        return name;
    }

    public Map<MannequinScanEnum, String> getTagMap() {
        return tagMap;
    }

    private void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidMannequinNameException("Empty mannequin name given.");
        }
    }

    private void validateAllScanPointsPresent(Map<MannequinScanEnum, String> tagMap) {
        for (MannequinScanEnum scanPoint : MannequinScanEnum.values()) {
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

        Mannequin mannequin = (Mannequin) o;

        return new EqualsBuilder()
                .append(name, mannequin.name)
                .append(tagMap, mannequin.tagMap)
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
