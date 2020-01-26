package edus2.adapter.repository.file;

import edus2.adapter.repository.memory.InMemoryManikinRepository;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;
import edus2.domain.ManikinTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static edus2.TestUtil.*;
import static org.junit.Assert.assertEquals;

public class FileManikinImportExportRepositoryTest extends ManikinTestBase {

    private ManikinFacade manikinFacade;
    private FileManikinImportExportRepository repository;

    @Before
    public void setup() {
        manikinFacade = new ManikinFacade(new InMemoryManikinRepository());
        repository = new FileManikinImportExportRepository(manikinFacade);
    }

    @Test
    public void exportImport_endToEnd() throws IOException {
        // Arrange
        Manikin manikin = new Manikin(generateTagMap(), randomAlphanumericString());
        String fileName = randomTempFile();
        manikinFacade.create(manikin);
        repository.exportManikinsToFile(new File(fileName));
        manikinFacade.remove(manikin);

        // Act
        repository.importManikinsFromFile(new File(fileName));

        // Assert
        List<Manikin> actual = manikinFacade.getAllManikins();
        assertEquals(Lst(manikin), actual);
    }
}