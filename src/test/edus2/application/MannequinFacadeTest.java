package edus2.application;

import edus2.adapter.repository.memory.InMemoryMannequinRepository;
import edus2.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class MannequinFacadeTest extends MannequinTestBase {

    private MannequinFacade facade;
    private InMemoryMannequinRepository mannequinRepository;

    @Before
    public void setup() {
        mannequinRepository = new InMemoryMannequinRepository();
        this.facade = new MannequinFacade(mannequinRepository);
    }

    @Test
    public void getMannequinNames_shouldReturnEmptySet_whenNoMannequinsExist() {
        // Act
        Set<String> actual = facade.getMannequinNames();

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getMannequinNames_shouldReturnMannequinNames_whenMannequinsExist() {
        // Arrange
        Mannequin mannequin = new Mannequin(generateTagMap(), "first");
        Mannequin secondMannequin = new Mannequin(generateTagMap(), "second");
        mannequinRepository.save(mannequin);
        mannequinRepository.save(secondMannequin);

        // Act
        Set<String> actual = facade.getMannequinNames();

        // Assert
        assertTrue(actual.contains("first"));
        assertTrue(actual.contains("second"));
        assertEquals(2, actual.size());
    }

    @Test (expected = DuplicateScanTagException.class)
    public void save_shouldThrowException_whenTagScanExistsForAnotherMannequin() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = generateTagMap();
        Map<MannequinScanEnum, String> secondTagMap = generateTagMap();
        secondTagMap.put(MannequinScanEnum.RIGHT_LUNG, tagMap.get(MannequinScanEnum.RIGHT_LUNG));
        Mannequin first = new Mannequin(tagMap, "first");
        Mannequin second = new Mannequin(secondTagMap, "second");
        facade.save(first);

        // Act
        facade.save(second);
    }

    @Test
    public void save_shouldNotThrowException_whenTagScansAllUnique() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = generateTagMap();
        Mannequin first = new Mannequin(tagMap, "first");

        // Act
        facade.save(first);
    }

    @Test
    public void save_shouldNotThrowException_whenUpdatingMannequin() {
        // Arrange
        Map<MannequinScanEnum, String> tagMap = generateTagMap();
        HashMap<MannequinScanEnum, String> updatedMap = new HashMap<>(tagMap);
        updatedMap.put(MannequinScanEnum.RIGHT_LUNG, tagMap.get(MannequinScanEnum.RIGHT_LUNG) + "-new");
        Mannequin mannequin = new Mannequin(tagMap, "mannequin");
        facade.save(mannequin);

        // Act
        facade.save(new Mannequin(updatedMap, "mannequin"));
    }

    @Test
    public void rename_shouldRenameMannequin_whenMannequinExists() {
        // Arrange
        Mannequin mannequin = new Mannequin(generateTagMap(), "mannequin");
        facade.save(mannequin);

        // Act
        facade.rename("mannequin", "updated");

        // Assert
        assertTrue(facade.getMannequin("updated").isPresent());
        assertFalse(facade.getMannequin("mannequin").isPresent());
        assertEquals(mannequin.getTagMap(), facade.getMannequin("updated").get().getTagMap());
    }

    @Test (expected = InvalidMannequinNameException.class)
    public void rename_shouldThrowException_whenCurrentNameDoesNotExist() {
        // Act
        facade.rename("name", "newName");
    }

    @Test (expected = InvalidMannequinNameException.class)
    public void rename_shouldThrowException_whenDesiredNameExists() {
        // Arrange
        Mannequin mannequin = new Mannequin(generateTagMap(), "mannequin");
        Mannequin second = new Mannequin(generateTagMap(), "second");
        facade.save(mannequin);
        facade.save(second);

        // Act
        facade.rename("mannequin", "second");
    }
}