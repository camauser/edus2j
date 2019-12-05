package edus2.adapter.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import edus2.application.ScanFacade;
import edus2.domain.MannequinScanEnum;
import edus2.domain.Scan;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class FileScanImportExportRepository {
    private ScanFacade scanFacade;
    private Gson gson;

    @Inject
    public FileScanImportExportRepository(ScanFacade scanFacade) {
        this.scanFacade = scanFacade;
        this.gson = new GsonBuilder().create();
    }

    public void exportScansToFile(File outputFile) throws IOException {
        List<ScanDto> scanDtos = scanFacade.getAllScans().stream().map(ScanDto::new).collect(Collectors.toList());
        Files.write(outputFile.toPath(), gson.toJson(scanDtos).getBytes());
    }

    public void importScansFromFile(File inputFile) throws IOException {
        byte[] fileContents = Files.readAllBytes(inputFile.toPath());
        Type type = new TypeToken<List<ScanDto>>(){}.getType();
        List<ScanDto> scanDtos = gson.fromJson(new String(fileContents), type);
        List<Scan> scans = scanDtos.stream().map(ScanDto::toScan).collect(Collectors.toList());
        scanFacade.addScans(scans);
    }

    private static class ScanDto {
        private MannequinScanEnum scanEnum;
        private String path;
        ScanDto(Scan scan) {
            this.scanEnum = scan.getScanEnum();
            this.path = scan.getPath();
        }

        Scan toScan() {
            return new Scan(scanEnum, this.path);
        }
    }
}
