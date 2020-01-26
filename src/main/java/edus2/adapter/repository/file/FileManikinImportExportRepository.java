package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;
import edus2.domain.ManikinScanEnum;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileManikinImportExportRepository {
    private ManikinFacade manikinFacade;
    private Gson gson;

    @Inject
    public FileManikinImportExportRepository(ManikinFacade manikinFacade) {
        this.manikinFacade = manikinFacade;
        this.gson = new GsonBuilder().create();
    }

    public void exportManikinsToFile(File outputFile) throws IOException {
        List<ManikinDto> manikinDtos = manikinFacade.getAllManikins().stream().map(ManikinDto::new).collect(Collectors.toList());
        Files.write(outputFile.toPath(), gson.toJson(manikinDtos).getBytes());
    }

    public void importManikinsFromFile(File inputFile) throws IOException {
        byte[] fileContents = Files.readAllBytes(inputFile.toPath());
        Type type = new TypeToken<List<ManikinDto>>(){}.getType();
        List<ManikinDto> manikinDtos = gson.fromJson(new String(fileContents), type);
        List<Manikin> manikins = manikinDtos.stream().map(ManikinDto::toManikin).collect(Collectors.toList());
        for (Manikin manikin : manikins) {
            manikinFacade.create(manikin);
        }
    }

    private static class ManikinDto {
        private String name;
        private Map<String, String> tagMap;

        ManikinDto(Manikin manikin) {
            this.name = manikin.getName();
            this.tagMap = new HashMap<>();
            for (Map.Entry<ManikinScanEnum, String> entry : manikin.getTagMap().entrySet()) {
                this.tagMap.put(entry.getKey().getName(), entry.getValue());
            }
        }

        Manikin toManikin() {
            Map<ManikinScanEnum, String> tagMap = new HashMap<>();
            for (Map.Entry<String, String> entry : this.tagMap.entrySet()) {
                tagMap.put(ManikinScanEnum.findByName(entry.getKey()), entry.getValue());
            }

            return new Manikin(tagMap, this.name);
        }
    }
}
