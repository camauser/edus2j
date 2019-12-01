package edus2.application;

import edus2.adapter.repository.memory.InMemoryMannequinRepository;
import edus2.domain.Mannequin;
import edus2.domain.MannequinTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}