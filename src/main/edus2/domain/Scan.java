package edus2.domain;/*
 * Copyright 2016 Paul Kulyk, Paul Olszynski, Cameron Auser
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;


public class Scan implements Serializable
{
    private MannequinScanEnum scanEnum;
    private String path;

    public Scan(MannequinScanEnum scanEnum, String path)
    {
        this.scanEnum = scanEnum;
        this.path = path;
    }

    public MannequinScanEnum getScanEnum()
    {
        return scanEnum;
    }

    public String getPath()
    {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Scan scan = (Scan) o;

        return new EqualsBuilder()
                .append(scanEnum, scan.scanEnum)
                .append(path, scan.path)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(scanEnum)
                .append(path)
                .toHashCode();
    }
}
