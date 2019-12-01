package edus2.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static edus2.TestUtil.randomAlphanumericString;
import static org.junit.Assert.*;

public abstract class MannequinRepositoryTest extends MannequinTestBase {

    private MannequinRepository repository;

    public abstract MannequinRepository getMannequinRepository();

    @Before
    public void setup() {
        repository = getMannequinRepository();
    }

    @Test
    public void retrieve_shouldReturnEmpty_whenNameDoesNotExist() {
        // Act
        Optional<Mannequin> actual = repository.retrieve(randomAlphanumericString());

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void retrieve_shouldReturnMannequin_whenNameExists() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = generateTagMap();
        repository.save(new Mannequin(tagMap, "mannequin"));

        // Act
        Optional<Mannequin> actual = repository.retrieve("mannequin");

        // Assert
        assertTrue(actual.isPresent());
        assertEquals("mannequin", actual.get().getName());
    }

    @Test
    public void retrieve_shouldReturnMannequin_whenNameExistsAndMultipleMannequinsExist() {
        // Arrange
        String expected = "thirdMannequin";
        repository.save(new Mannequin(generateTagMap(), "firstMannequin"));
        repository.save(new Mannequin(generateTagMap(), "secondMannequin"));
        repository.save(new Mannequin(generateTagMap(), expected));

        // Act
        Optional<Mannequin> actual = repository.retrieve(expected);

        // Assert
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get().getName());
    }

    @Test
    public void retrieveAll_shouldReturnEmpty_whenNoMannequinsExist() {
        // Act
        List<Mannequin> actual = repository.retrieveAll();

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    public void retrieveAll_shouldReturnAllMannequins_whenMannequinsExist() {
        // Arrange
        String firstExpected = "firstMannequin";
        String secondExpected = "secondMannequin";
        repository.save(new Mannequin(generateTagMap(), firstExpected));
        repository.save(new Mannequin(generateTagMap(), secondExpected));

        // Act
        List<Mannequin> mannequins = repository.retrieveAll();

        // Assert
        assertEquals(2, mannequins.size());
        assertTrue(mannequins.stream().anyMatch(m -> m.getName().equals(firstExpected)));
        assertTrue(mannequins.stream().anyMatch(m -> m.getName().equals(secondExpected)));
    }

    @Test
    public void save_shouldSaveMannequin_whenMannequinDoesNotExist() {
        // Arrange
        String expected = "mannequin";

        // Act
        repository.save(new Mannequin(generateTagMap(), expected));

        // Assert
        Optional<Mannequin> actual = repository.retrieve(expected);
        assertTrue(actual.isPresent());
    }

    @Test
    public void save_shouldUpdateMannequin_whenMannequinExists() {
        // Arrange
        String name = "mannequin";
        Map<MannequinScanEnum, String> tagMap = generateTagMap();
        Mannequin mannequin = new Mannequin(duplicateMap(tagMap), name);
        repository.save(mannequin);
        String expected = tagMap.get(MannequinScanEnum.RIGHT_LUNG) + "-updated";
        tagMap.put(MannequinScanEnum.RIGHT_LUNG, expected);

        // Act
        repository.save(new Mannequin(tagMap, name));

        // Assert
        Optional<Mannequin> actual = repository.retrieve(name);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get().getTagMap().get(MannequinScanEnum.RIGHT_LUNG));
    }

    @Test
    public void remove_shouldDoNothing_whenMannequinDoesNotExist() {
        // Act
        repository.remove(randomAlphanumericString());
    }

    @Test
    public void remove_shouldRemoveMannequin_whenMannequinExists() {
        // Arrange
        String name = "mannequin";
        repository.save(new Mannequin(generateTagMap(), name));

        // Act
        repository.remove(name);

        // Assert
        Optional<Mannequin> actual = repository.retrieve(name);
        assertFalse(actual.isPresent());
    }

    private <K, V> Map<K, V> duplicateMap(Map<K, V> original) {
        return new HashMap<>(original);
    }
}