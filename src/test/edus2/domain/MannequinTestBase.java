package edus2.domain;

import java.util.HashMap;
import java.util.Map;

import static edus2.TestUtil.randomAlphanumericString;

public abstract class MannequinTestBase {
    protected Map<MannequinScanEnum, String> generateTagMap() {
        Map<MannequinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(MannequinScanEnum.RIGHT_LUNG, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.LEFT_LUNG, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.CARDIAC_PSL_PSS, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.CARDIAC_A4C, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.CARDIAC_SC, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.IVC, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.RUQ, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.LUQ, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.ABDOMINAL_AORTA, randomAlphanumericString());
        tagMap.put(MannequinScanEnum.PELVIS, randomAlphanumericString());
        return tagMap;
    }
}
