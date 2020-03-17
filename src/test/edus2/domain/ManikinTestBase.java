package edus2.domain;

import java.util.HashMap;
import java.util.Map;

import static edus2.TestUtil.randomAlphanumericString;

public abstract class ManikinTestBase {
    public static Map<ManikinScanEnum, String> generateTagMap() {
        Map<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.LEFT_LUNG, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.CARDIAC_SC, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.IVC, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.RUQ, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.LUQ, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.PELVIS, randomAlphanumericString());
        return tagMap;
    }
}
