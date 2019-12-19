package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.application.MannequinFacade;
import edus2.domain.Mannequin;
import edus2.domain.MannequinScanEnum;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileMannequinImportExportRepository {
    private MannequinFacade mannequinFacade;
    private Gson gson;

    @Inject
    public FileMannequinImportExportRepository(MannequinFacade mannequinFacade) {
        this.mannequinFacade = mannequinFacade;
        this.gson = new GsonBuilder().create();
    }

    public void exportMannequinsToFile(File outputFile) throws IOException {
        List<MannequinDto> mannequinDtos = mannequinFacade.getAllMannequins().stream().map(MannequinDto::new).collect(Collectors.toList());
        Files.write(outputFile.toPath(), gson.toJson(mannequinDtos).getBytes());
    }

    public void importMannequinsFromFile(File inputFile) throws IOException {
        byte[] fileContents = Files.readAllBytes(inputFile.toPath());
        Type type = new TypeToken<List<MannequinDto>>(){}.getType();
        List<MannequinDto> mannequinDtos = gson.fromJson(new String(fileContents), type);
        List<Mannequin> mannequins = mannequinDtos.stream().map(MannequinDto::toMannequin).collect(Collectors.toList());
        for (Mannequin mannequin : mannequins) {
            mannequinFacade.create(mannequin);
        }
    }

    private static class MannequinDto {
        private String name;
        private Map<String, String> tagMap;

        MannequinDto(Mannequin mannequin) {
            this.name = mannequin.getName();
            this.tagMap = new HashMap<>();
            for (Map.Entry<MannequinScanEnum, String> entry : mannequin.getTagMap().entrySet()) {
                this.tagMap.put(entry.getKey().getName(), entry.getValue());
            }
        }

        Mannequin toMannequin() {
            Map<MannequinScanEnum, String> tagMap = new HashMap<>();
            for (Map.Entry<String, String> entry : this.tagMap.entrySet()) {
                tagMap.put(MannequinScanEnum.findByName(entry.getKey()), entry.getValue());
            }

            return new Mannequin(tagMap, this.name);
        }
    }
}
