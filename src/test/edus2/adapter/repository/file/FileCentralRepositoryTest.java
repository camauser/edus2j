package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.adapter.repository.file.dto.ScanDto;
import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.domain.EDUS2Configuration;
import edus2.domain.Scan;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edus2.TestUtil.randomAlphanumericString;
import static edus2.TestUtil.randomManikinScanEnum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileCentralRepositoryTest {

    private FileCentralRepository repository;
    private String sectionName;

    @Before
    public void setup() {
        String fileName = randomAlphanumericString();
        File file = new File(fileName);
        file.deleteOnExit();
        EDUS2Configuration configuration = new InMemoryEDUS2Configuration();
        configuration.setSaveFileLocation(file.getAbsolutePath());
        this.sectionName = "hello";
        this.repository = new FileCentralRepository(configuration) {
            @Override
            protected String getSectionName() {
                return sectionName;
            }
        };
    }

    @Test
    public void retrieveSection_shouldReturnEmpty_whenFileDoesNotContainSection() {
        // Act
        Optional<String> actual = repository.retrieveSection();

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void saveSection_shouldSaveSection_whenSectionDoesNotExist() {
        // Act
        String expected = "world";
        repository.saveSection(expected);

        // Assert
        Optional<String> actual = repository.retrieveSection();
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void saveSection_shouldOverwriteSection_whenSectionExists() {
        // Arrange
        String expected = "updated";
        repository.saveSection("world");

        // Act
        repository.saveSection(expected);

        // Assert
        Optional<String> actual = repository.retrieveSection();
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void saveSection_shouldPersistJsonCorrectly() {
        // Arrange
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<ScanDto>>(){}.getType();
        ArrayList<ScanDto> scans = new ArrayList<>();
        scans.add(new ScanDto(new Scan(randomManikinScanEnum(), Paths.get("path"))));
        scans.add(new ScanDto(new Scan(randomManikinScanEnum(), Paths.get("path2"))));

        // Act
        repository.saveSection(gson.toJson(scans));

        // Assert
        Optional<List<ScanDto>> actual = repository.retrieveSection().map(j -> gson.fromJson(j, type));
        assertEquals(Optional.of(scans), actual);
    }
}