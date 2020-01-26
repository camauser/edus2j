package edus2.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static edus2.TestUtil.randomAlphanumericString;
import static org.junit.Assert.*;

public abstract class ManikinRepositoryTest extends ManikinTestBase {

    private ManikinRepository repository;

    public abstract ManikinRepository getManikinRepository();

    @Before
    public void setup() {
        repository = getManikinRepository();
    }

    @Test
    public void retrieve_shouldReturnEmpty_whenNameDoesNotExist() {
        // Act
        Optional<Manikin> actual = repository.retrieve(randomAlphanumericString());

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void retrieve_shouldReturnManikin_whenNameExists() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = generateTagMap();
        repository.save(new Manikin(tagMap, "manikin"));

        // Act
        Optional<Manikin> actual = repository.retrieve("manikin");

        // Assert
        assertTrue(actual.isPresent());
        assertEquals("manikin", actual.get().getName());
    }

    @Test
    public void retrieve_shouldReturnManikin_whenNameExistsAndMultipleManikinsExist() {
        // Arrange
        String expected = "thirdManikin";
        repository.save(new Manikin(generateTagMap(), "firstManikin"));
        repository.save(new Manikin(generateTagMap(), "secondManikin"));
        repository.save(new Manikin(generateTagMap(), expected));

        // Act
        Optional<Manikin> actual = repository.retrieve(expected);

        // Assert
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get().getName());
    }

    @Test
    public void retrieveAll_shouldReturnEmpty_whenNoManikinsExist() {
        // Act
        List<Manikin> actual = repository.retrieveAll();

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    public void retrieveAll_shouldReturnAllManikins_whenManikinsExist() {
        // Arrange
        String firstExpected = "firstManikin";
        String secondExpected = "secondManikin";
        repository.save(new Manikin(generateTagMap(), firstExpected));
        repository.save(new Manikin(generateTagMap(), secondExpected));

        // Act
        List<Manikin> manikins = repository.retrieveAll();

        // Assert
        assertEquals(2, manikins.size());
        assertTrue(manikins.stream().anyMatch(m -> m.getName().equals(firstExpected)));
        assertTrue(manikins.stream().anyMatch(m -> m.getName().equals(secondExpected)));
    }

    @Test
    public void save_shouldSaveManikin_whenManikinDoesNotExist() {
        // Arrange
        String expected = "manikin";

        // Act
        repository.save(new Manikin(generateTagMap(), expected));

        // Assert
        Optional<Manikin> actual = repository.retrieve(expected);
        assertTrue(actual.isPresent());
    }

    @Test
    public void save_shouldUpdateManikin_whenManikinExists() {
        // Arrange
        String name = "manikin";
        Map<ManikinScanEnum, String> tagMap = generateTagMap();
        Manikin manikin = new Manikin(duplicateMap(tagMap), name);
        repository.save(manikin);
        String expected = tagMap.get(ManikinScanEnum.RIGHT_LUNG) + "-updated";
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, expected);

        // Act
        repository.save(new Manikin(tagMap, name));

        // Assert
        Optional<Manikin> actual = repository.retrieve(name);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get().getTagMap().get(ManikinScanEnum.RIGHT_LUNG));
    }

    @Test
    public void remove_shouldDoNothing_whenManikinDoesNotExist() {
        // Act
        repository.remove(randomAlphanumericString());
    }

    @Test
    public void remove_shouldRemoveManikin_whenManikinExists() {
        // Arrange
        String name = "manikin";
        repository.save(new Manikin(generateTagMap(), name));

        // Act
        repository.remove(name);

        // Assert
        Optional<Manikin> actual = repository.retrieve(name);
        assertFalse(actual.isPresent());
    }

    private <K, V> Map<K, V> duplicateMap(Map<K, V> original) {
        return new HashMap<>(original);
    }
}