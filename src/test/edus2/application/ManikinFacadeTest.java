package edus2.application;

import edus2.adapter.repository.memory.InMemoryManikinRepository;
import edus2.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class ManikinFacadeTest extends ManikinTestBase {

    private ManikinFacade facade;
    private InMemoryManikinRepository manikinRepository;

    @Before
    public void setup() {
        manikinRepository = new InMemoryManikinRepository();
        this.facade = new ManikinFacade(manikinRepository);
    }

    @Test
    public void getManikinNames_shouldReturnEmptySet_whenNoManikinsExist() {
        // Act
        Set<String> actual = facade.getManikinNames();

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getManikinNames_shouldReturnManikinNames_whenManikinsExist() {
        // Arrange
        Manikin manikin = new Manikin(generateTagMap(), "first");
        Manikin secondManikin = new Manikin(generateTagMap(), "second");
        manikinRepository.save(manikin);
        manikinRepository.save(secondManikin);

        // Act
        Set<String> actual = facade.getManikinNames();

        // Assert
        assertTrue(actual.contains("first"));
        assertTrue(actual.contains("second"));
        assertEquals(2, actual.size());
    }

    @Test (expected = DuplicateScanTagException.class)
    public void create_shouldThrowException_whenTagScanExistsForAnotherManikin() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = generateTagMap();
        Map<ManikinScanEnum, String> secondTagMap = generateTagMap();
        secondTagMap.put(ManikinScanEnum.RIGHT_LUNG, tagMap.get(ManikinScanEnum.RIGHT_LUNG));
        Manikin first = new Manikin(tagMap, "first");
        Manikin second = new Manikin(secondTagMap, "second");
        facade.create(first);

        // Act
        facade.create(second);
    }

    @Test
    public void create_shouldNotThrowException_whenTagScansAllUnique() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = generateTagMap();
        Manikin first = new Manikin(tagMap, "first");

        // Act
        facade.create(first);
    }

    @Test
    public void update_shouldNotThrowException_validUpdateGiven() {
        // Arrange
        Map<ManikinScanEnum, String> tagMap = generateTagMap();
        HashMap<ManikinScanEnum, String> updatedMap = new HashMap<>(tagMap);
        updatedMap.put(ManikinScanEnum.RIGHT_LUNG, tagMap.get(ManikinScanEnum.RIGHT_LUNG) + "-new");
        Manikin manikin = new Manikin(tagMap, "manikin");
        facade.create(manikin);

        // Act
        facade.update(new Manikin(updatedMap, "manikin"));
    }

    @Test
    public void rename_shouldRenameManikin_whenManikinExists() {
        // Arrange
        Manikin manikin = new Manikin(generateTagMap(), "manikin");
        facade.create(manikin);

        // Act
        facade.rename("manikin", "updated");

        // Assert
        assertTrue(facade.getManikin("updated").isPresent());
        assertFalse(facade.getManikin("manikin").isPresent());
        assertEquals(manikin.getTagMap(), facade.getManikin("updated").get().getTagMap());
    }

    @Test (expected = InvalidManikinNameException.class)
    public void rename_shouldThrowException_whenCurrentNameDoesNotExist() {
        // Act
        facade.rename("name", "newName");
    }

    @Test (expected = InvalidManikinNameException.class)
    public void rename_shouldThrowException_whenDesiredNameExists() {
        // Arrange
        Manikin manikin = new Manikin(generateTagMap(), "manikin");
        Manikin second = new Manikin(generateTagMap(), "second");
        facade.update(manikin);
        facade.update(second);

        // Act
        facade.rename("manikin", "second");
    }

    @Test
    public void create_shouldCreateNewManikin() {
        // Arrange
        Manikin manikin = new Manikin(generateTagMap(), "manikin");

        // Act
        facade.create(manikin);

        // Assert
        Optional<Manikin> actual = facade.getManikin(manikin.getName());
        assertTrue(actual.isPresent());
    }

    @Test (expected = InvalidManikinNameException.class)
    public void create_shouldThrowException_whenNameExists() {
        // Arrange
        Manikin manikin = new Manikin(generateTagMap(), "manikin");
        Manikin secondManikin = new Manikin(generateTagMap(), "manikin");
        facade.create(manikin);

        // Act
        facade.create(secondManikin);
    }
}