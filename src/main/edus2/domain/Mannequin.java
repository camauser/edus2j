package edus2.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Mannequin {
    private final String name;
    private final Map<MannequinScanEnum, String> tagMap;

    public Mannequin(Map<MannequinScanEnum, String> tagMap, String name) {
        validateAllScanPointsPresent(tagMap);
        this.name = name;
        this.tagMap = tagMap;
    }

    private void validateAllScanPointsPresent(Map<MannequinScanEnum, String> tagMap) {
        for (MannequinScanEnum scanPoint : MannequinScanEnum.values()) {
            if (!tagMap.containsKey(scanPoint)) {
                throw new MissingRequiredScanPointException(String.format("Missing entry for %s", scanPoint.getName()));
            }
        }

        Set<String> scans = new HashSet<>();
        for (String scan : tagMap.values()) {
            if (scans.contains(scan)) {
                throw new DuplicateScanTagException(String.format("Scan tag %s was listed more than once", scan));
            }
            scans.add(scan);
        }
    }
}
