package edus2.domain;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ManikinTest {

    @Test (expected = MissingRequiredScanPointException.class)
    public void constructor_shouldThrowException_whenEnumMissingFromMap() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, "sampleTag1");
        tagMap.put(ManikinScanEnum.LEFT_LUNG, "sampleTag2");
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(ManikinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(ManikinScanEnum.IVC, "sampleTag6");
        tagMap.put(ManikinScanEnum.RUQ, "sampleTag7");
        tagMap.put(ManikinScanEnum.LUQ, "sampleTag8");
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(ManikinScanEnum.PELVIS, "sampleTag10");

        tagMap.remove(ManikinScanEnum.RIGHT_LUNG);

        // Act
        new Manikin(tagMap, "Sample Name");
    }

    @Test
    public void constructor_shouldNotThrowException_whenAllEnumsPresent() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, "sampleTag1");
        tagMap.put(ManikinScanEnum.LEFT_LUNG, "sampleTag2");
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(ManikinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(ManikinScanEnum.IVC, "sampleTag6");
        tagMap.put(ManikinScanEnum.RUQ, "sampleTag7");
        tagMap.put(ManikinScanEnum.LUQ, "sampleTag8");
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(ManikinScanEnum.PELVIS, "sampleTag10");

        // Act
        new Manikin(tagMap, "Sample Name");
    }

    @Test (expected = DuplicateScanTagException.class)
    public void constructor_shouldThrowException_whenSameScanUsedForMultipleLocations() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, "duplicateTag");
        tagMap.put(ManikinScanEnum.LEFT_LUNG, "duplicateTag");
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(ManikinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(ManikinScanEnum.IVC, "sampleTag6");
        tagMap.put(ManikinScanEnum.RUQ, "sampleTag7");
        tagMap.put(ManikinScanEnum.LUQ, "sampleTag8");
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(ManikinScanEnum.PELVIS, "sampleTag10");

        // Act
        new Manikin(tagMap, "Sample Name");
    }

    @Test (expected = MissingRequiredScanPointException.class)
    public void constructor_shouldThrowException_whenGivenNullValueInTagMap() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, null);
        tagMap.put(ManikinScanEnum.LEFT_LUNG, "sampleTag2");
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(ManikinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(ManikinScanEnum.IVC, "sampleTag6");
        tagMap.put(ManikinScanEnum.RUQ, "sampleTag7");
        tagMap.put(ManikinScanEnum.LUQ, "sampleTag8");
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(ManikinScanEnum.PELVIS, "sampleTag10");

        // Act
        new Manikin(tagMap, "Sample Name");
    }

    @Test (expected = InvalidManikinNameException.class)
    public void constructor_shouldThrowException_whenGivenNullName() {
        // Act
        new Manikin(new HashMap<>(), null);
    }

    @Test (expected = InvalidManikinNameException.class)
    public void constructor_shouldThrowException_whenGivenEmptyName() {
        // Act
        new Manikin(new HashMap<>(), "");
    }
}