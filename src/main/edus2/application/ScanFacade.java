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

import edus2.adapter.FileScanRepository;
import edus2.domain.Scan;
import edus2.adapter.logging.LoggerSingleton;
import edus2.domain.ScanRepository;

import java.util.*;

public class ScanFacade {
    private ScanRepository scanRepository;

    public ScanFacade() {
        scanRepository = new FileScanRepository(FileScanRepository.FILE_NAME);
    }

    public Optional<Scan> getScan(String id) {
        Optional<Scan> scan = getAllScans().stream().filter(s -> s.getId().equals(id)).findFirst();
        if (scan.isPresent()) {
            LoggerSingleton.logInfoIfEnabled("The path for scan \"" + id + "\" appears to be " + scan.get().getPath());
        } else {
            LoggerSingleton.logInfoIfEnabled("Scan \"" + id + "\" doesn't exist in the system");
        }

        return scan;
    }

    public boolean containsScan(String id) {
        return getAllScans().stream().anyMatch(s -> s.getId().equals(id));
    }

    public void addScan(Scan scan) {
        scanRepository.save(scan);
    }

    public void addScans(Collection<Scan> scans) {
        for (Scan scan : scans) {
            addScan(scan);
        }
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

    public String toCSV() {
        StringBuilder stringBuffer = new StringBuilder();
        for (Scan scan : getAllScans()) {
            stringBuffer.append(scan.getId());
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
            String id = currentLine.split(",")[0];
            String scan = currentLine.split(",")[1];
            if (!containsScan(id)) {
                getAllScans().add(new Scan(id, scan));
            }
        }
    }
}
