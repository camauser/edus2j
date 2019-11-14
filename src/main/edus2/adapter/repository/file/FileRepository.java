package edus2.adapter.repository.file;

import java.io.*;
import java.util.Optional;

public abstract class FileRepository {

    protected Optional<String> readFileContents(String fileName) {
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

    protected void saveToFile(String content, String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to save to " + fileName + ". Error: " + e.getMessage(), e);
        }
    }
}
