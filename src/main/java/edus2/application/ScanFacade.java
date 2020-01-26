package edus2.application;/*
 * Copyright 2018 Paul Kulyk, Paul Olszynski, Cameron Auser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import edus2.application.exception.EmptyScanIdException;
import edus2.application.exception.ScanAlreadyExistsException;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import edus2.domain.ScanRepository;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class ScanFacade {
    private ScanRepository scanRepository;

    @Inject
    public ScanFacade(ScanRepository scanRepository) {
        this.scanRepository = scanRepository;
    }

    public Optional<Scan> getScan(ManikinScanEnum scanEnum) {
        return getAllScans().stream().filter(s -> s.getScanEnum().equals(scanEnum)).findFirst();
    }

    public boolean containsScan(ManikinScanEnum scanEnum) {
        return getAllScans().stream().anyMatch(s -> s.getScanEnum().equals(scanEnum));
    }

    public void addScan(Scan scan) {
        if (scan.getScanEnum() == null) {
            throw new EmptyScanIdException();
        }

        if (containsScanEnum(scan.getScanEnum())) {
            throw new ScanAlreadyExistsException(scan.getScanEnum());
        }

        scanRepository.save(scan);
    }

    private boolean containsScanEnum(ManikinScanEnum scanEnum) {
        return getAllScans().stream().map(Scan::getScanEnum).anyMatch(e -> e.equals(scanEnum));
    }

    public void addScans(Collection<Scan> scans) {
        scans.forEach(this::addScan);
    }

    public void removeScan(Scan scan) {
        scanRepository.remove(scan);
    }

    public void removeAllScans() {
        scanRepository.removeAll();
    }

    public List<Scan> getAllScans() {
        return scanRepository.retrieveAll();
    }

    public int scanCount() {
        return getAllScans().size();
    }

    public Set<ManikinScanEnum> getUnusedScanEnums() {
        Set<ManikinScanEnum> unusedScanEnums = new HashSet<>(Arrays.asList(ManikinScanEnum.values()));
        Set<ManikinScanEnum> usedScanEnums = getAllScans().stream().map(Scan::getScanEnum).collect(Collectors.toSet());
        unusedScanEnums.removeAll(usedScanEnums);
        return unusedScanEnums;
    }

    public String toCSV() {
        StringBuilder stringBuffer = new StringBuilder();
        for (Scan scan : getAllScans()) {
            stringBuffer.append(scan.getScanEnum());
            stringBuffer.append(',');
            stringBuffer.append(scan.getPath());
            stringBuffer.append('\n');
        }
        return stringBuffer.toString();
    }

    public void importCSV(String csv) {
        Scanner scanner = new Scanner(csv);
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            ManikinScanEnum scanEnum = ManikinScanEnum.valueOf(currentLine.split(",")[0]);
            String path = currentLine.split(",")[1];
            if (!containsScan(scanEnum)) {
                Scan scan = new Scan(scanEnum, path);
                scanRepository.save(scan);
            }
        }
    }
}
