package edus2.adapter.repository.file.dto;

import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;

import java.nio.file.Paths;
import java.util.Objects;

public class ScanDto {

    private final String scanEnum;
    private final String path;

    public ScanDto(Scan scan) {
        this.scanEnum = scan.getScanEnum().name();
        this.path = scan.getPath().toString();
    }

    public ScanDto(String scanEnum, String path) {
        this.scanEnum = scanEnum;
        this.path = path;
    }

    public String getScanEnum() {
        return scanEnum;
    }

    public String getPath() {
        return path;
    }

    public Scan toDomain() {
        return new Scan(ManikinScanEnum.valueOf(scanEnum), Paths.get(path));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanDto scanDto = (ScanDto) o;
        return Objects.equals(scanEnum, scanDto.scanEnum) && Objects.equals(path, scanDto.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scanEnum, path);
    }

    @Override
    public String toString() {
        return "ScanDto{" +
                "scanEnum='" + scanEnum + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
