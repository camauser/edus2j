package edus2.domain;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MannequinTest {

    @Test (expected = MissingRequiredScanPointException.class)
    public void constructor_shouldThrowException_whenEnumMissingFromMap() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(MannequinScanEnum.RIGHT_LUNG, "sampleTag1");
        tagMap.put(MannequinScanEnum.LEFT_LUNG, "sampleTag2");
        tagMap.put(MannequinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(MannequinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(MannequinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(MannequinScanEnum.IVC, "sampleTag6");
        tagMap.put(MannequinScanEnum.RUQ, "sampleTag7");
        tagMap.put(MannequinScanEnum.LUQ, "sampleTag8");
        tagMap.put(MannequinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(MannequinScanEnum.PELVIS, "sampleTag10");

        tagMap.remove(MannequinScanEnum.RIGHT_LUNG);

        // Act
        new Mannequin(tagMap, "Sample Name");
    }

    @Test
    public void constructor_shouldNotThrowException_whenAllEnumsPresent() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(MannequinScanEnum.RIGHT_LUNG, "sampleTag1");
        tagMap.put(MannequinScanEnum.LEFT_LUNG, "sampleTag2");
        tagMap.put(MannequinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(MannequinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(MannequinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(MannequinScanEnum.IVC, "sampleTag6");
        tagMap.put(MannequinScanEnum.RUQ, "sampleTag7");
        tagMap.put(MannequinScanEnum.LUQ, "sampleTag8");
        tagMap.put(MannequinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(MannequinScanEnum.PELVIS, "sampleTag10");

        // Act
        new Mannequin(tagMap, "Sample Name");
    }

    @Test (expected = DuplicateScanTagException.class)
    public void constructor_shouldThrowException_whenSameScanUsedForMultipleLocations() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(MannequinScanEnum.RIGHT_LUNG, "duplicateTag");
        tagMap.put(MannequinScanEnum.LEFT_LUNG, "duplicateTag");
        tagMap.put(MannequinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(MannequinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(MannequinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(MannequinScanEnum.IVC, "sampleTag6");
        tagMap.put(MannequinScanEnum.RUQ, "sampleTag7");
        tagMap.put(MannequinScanEnum.LUQ, "sampleTag8");
        tagMap.put(MannequinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(MannequinScanEnum.PELVIS, "sampleTag10");

        // Act
        new Mannequin(tagMap, "Sample Name");
    }

    @Test (expected = MissingRequiredScanPointException.class)
    public void constructor_shouldThrowException_whenGivenNullValueInTagMap() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(MannequinScanEnum.RIGHT_LUNG, null);
        tagMap.put(MannequinScanEnum.LEFT_LUNG, "sampleTag2");
        tagMap.put(MannequinScanEnum.CARDIAC_PSL_PSS, "sampleTag3");
        tagMap.put(MannequinScanEnum.CARDIAC_A4C, "sampleTag4");
        tagMap.put(MannequinScanEnum.CARDIAC_SC, "sampleTag5");
        tagMap.put(MannequinScanEnum.IVC, "sampleTag6");
        tagMap.put(MannequinScanEnum.RUQ, "sampleTag7");
        tagMap.put(MannequinScanEnum.LUQ, "sampleTag8");
        tagMap.put(MannequinScanEnum.ABDOMINAL_AORTA, "sampleTag9");
        tagMap.put(MannequinScanEnum.PELVIS, "sampleTag10");

        // Act
        new Mannequin(tagMap, "Sample Name");
    }

    @Test (expected = InvalidMannequinNameException.class)
    public void constructor_shouldThrowException_whenGivenNullName() {
        // Act
        new Mannequin(new HashMap<>(), null);
    }

    @Test (expected = InvalidMannequinNameException.class)
    public void constructor_shouldThrowException_whenGivenEmptyName() {
        // Act
        new Mannequin(new HashMap<>(), "");
    }
}