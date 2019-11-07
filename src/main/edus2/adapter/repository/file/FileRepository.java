package edus2.adapter.repository.file;

import edus2.adapter.logging.LoggerSingleton;

import java.io.*;
import java.util.Optional;

public abstract class FileRepository {
    private final String fileName;

    public FileRepository(String fileName) {
        this.fileName = fileName;
    }
    protected Optional<String> readFileContents() {
        File file = new File(fileName);
        StringBuilder json = new StringBuilder();
        try {
            file.createNewFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    json.append(currentLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json.toString().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(json.toString());
    }

    protected void saveToFile(String content) {
        File file = new File(fileName);
        try {
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            }
        } catch (IOException e) {
            LoggerSingleton.logErrorIfEnabled("Unable to save to " + fileName + ". Error: " + e.getMessage());
        }
    }
}
