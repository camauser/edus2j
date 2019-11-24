package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.domain.Scan;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edus2.TestUtil.randomAlphanumericString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileCentralRepositoryTest {

    private FileCentralRepository repository;
    @Before
    public void setup() {
        String fileName = randomAlphanumericString();
        File file = new File(fileName);
        file.deleteOnExit();
        this.repository = new FileCentralRepository(file.getAbsolutePath());
    }

    @Test
    public void retrieveSection_shouldReturnEmpty_whenFileIsEmpty() {
        // Act
        Optional<String> actual = repository.retrieveSection(randomAlphanumericString());

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void retrieveSection_shouldReturnEmpty_whenFileDoesNotContainSection() {
        // Arrange
        repository.saveSection("hello", "world");

        // Act
        Optional<String> actual = repository.retrieveSection(randomAlphanumericString());

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void saveSection_shouldSaveSection_whenSectionDoesNotExist() {
        // Act
        String expected = "world";
        repository.saveSection("hello", expected);

        // Assert
        Optional<String> actual = repository.retrieveSection("hello");
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void saveSection_shouldOverwriteSection_whenSectionExists() {
        // Arrange
        String expected = "updated";
        repository.saveSection("hello", "world");

        // Act
        repository.saveSection("hello", expected);

        // Assert
        Optional<String> actual = repository.retrieveSection("hello");
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void saveSection_shouldPersistJsonCorrectly() {
        // Arrange
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<Scan>>(){}.getType();
        ArrayList<Scan> scans = new ArrayList<>();
        scans.add(new Scan("id", "path"));
        scans.add(new Scan("id2", "path2"));

        // Act
        repository.saveSection("scans", gson.toJson(scans));

        // Assert
        Optional<List<Scan>> actual = repository.retrieveSection("scans").map(j -> gson.fromJson(j, type));
        assertEquals(Optional.of(scans), actual);
    }
}