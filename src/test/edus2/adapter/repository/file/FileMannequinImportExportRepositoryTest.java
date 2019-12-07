package edus2.adapter.repository.file;

import edus2.adapter.repository.memory.InMemoryMannequinRepository;
import edus2.application.MannequinFacade;
import edus2.domain.Mannequin;
import edus2.domain.MannequinTestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static edus2.TestUtil.*;
import static org.junit.Assert.assertEquals;

public class FileMannequinImportExportRepositoryTest extends MannequinTestBase {

    private MannequinFacade mannequinFacade;
    private FileMannequinImportExportRepository repository;

    @Before
    public void setup() {
        mannequinFacade = new MannequinFacade(new InMemoryMannequinRepository());
        repository = new FileMannequinImportExportRepository(mannequinFacade);
    }

    @Test
    public void exportImport_endToEnd() throws IOException {
        // Arrange
        Mannequin mannequin = new Mannequin(generateTagMap(), randomAlphanumericString());
        String fileName = randomTempFile();
        mannequinFacade.create(mannequin);
        repository.exportMannequinsToFile(new File(fileName));
        mannequinFacade.remove(mannequin);

        // Act
        repository.importMannequinsFromFile(new File(fileName));

        // Assert
        List<Mannequin> actual = mannequinFacade.getAllMannequins();
        assertEquals(Lst(mannequin), actual);
    }
}