/*
 * Copyright 2016 Cameron Auser
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

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 *  Purpose: Keep track of everything involved with an EDUS2 scan in a single
 *  class.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class Scan implements Serializable
{
    // TODO : Make these variables private, use getters/setters instead
    // of direct references in other classes
    private String id;
    private String path;
    
    /**
     * 
     * Constructor for the Scan class.
     * @param id - The ID of the scan.
     * @param path - The path of the video.
     */
    public Scan(String id, String path)
    {
        this.id = id;
        this.path = path;
    }
    
    /**
     * Purpose: A toString method for the Scan class.
     */
    public String toString()
    {
        return "Scan[id = " + id + ", path = " + path + "]";
    }

    /**
     * 
     * Purpose: A method to get the ID of the scan.
     * @return - The ID of the scan.
     */
    public String getId()
    {
        return id;
    }

    /**
     * 
     * Purpose: A method to set the ID of the scan.
     * @param id - the new ID.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * 
     * Purpose: A method to get the scan video path.
     * @return - the video's path.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * 
     * Purpose: A method to set the scan video's path.
     * @param path - the new path
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * 
     * Purpose: Convert this object into CSV format.
     * @return - The scan, formatted as CSV.
     */
    public String toCSV()
    {
        return id + "," + path;
    }
}
