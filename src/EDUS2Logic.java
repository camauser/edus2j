/*
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

import logging.LoggerSingleton;

import java.util.*;

public class EDUS2Logic {
    private ArrayList<Scan> scans;
    public EDUS2Logic()
    {
        scans = new ArrayList<>();
    }

    public Optional<Scan> getScan(String id)
    {
        for(Scan scan : scans)
        {
            if (scan.getId().equals(id))
            {
                LoggerSingleton.logInfoIfEnabled("The path for scan \"" + id + "\" appears to be " + scan.getPath());
                return Optional.of(scan);
            }
        }
        LoggerSingleton.logInfoIfEnabled("Scan \"" + id + "\" doesn't exist in the system");
        return Optional.empty();
    }

    public boolean containsScan(String id)
    {
        for(Scan scan : scans)
        {
            if (scan.getId().equals(id))
            {
                return true;
            }
        }

        return false;
    }

    public void addScan(Scan scan)
    {
        scans.add(scan);
    }

    public void addMultipleScans(Collection<Scan> scans)
    {
        this.scans.addAll(scans);
    }

    public void removeScan(Scan scan)
    {
        scans.remove(scan);
    }

    public void removeAllScans()
    {
        scans.clear();
    }

    public ArrayList<Scan> getAllScans()
    {
        ArrayList<Scan> toReturn = new ArrayList<>();
        for(Scan scan : scans)
        {
            toReturn.add(scan);
        }

        return toReturn;
    }

    public int scanCount()
    {
        return scans.size();
    }

    public String toCSV()
    {
        StringBuffer stringBuffer = new StringBuffer();
        for(Scan scan : scans)
        {
            stringBuffer.append(scan.getId());
            stringBuffer.append(',');
            stringBuffer.append(scan.getPath());
            stringBuffer.append('\n');
        }
        return stringBuffer.toString();
    }

    public void importCSV(String csv)
    {
        Scanner scanner = new Scanner(csv);
        while(scanner.hasNextLine())
        {
            String currentLine = scanner.nextLine();
            String id = currentLine.split(",")[0];
            String scan = currentLine.split(",")[1];
            if (!containsScan(id))
            {
                scans.add(new Scan(id, scan));
            }
        }
    }
}
