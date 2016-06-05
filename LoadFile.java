/*
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

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Stack;

/**
 * 
 * Purpose: A utility class to load an object from a file.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class LoadFile
{

    /**
     * 
     * Purpose: A static method to load an object from a file, and then return
     * it.
     * 
     * @param fileName
     *            - The file to read from.
     * @return The object returned from the file.
     * @throws Exception
     */
    public static Object load(String fileName) throws Exception
    {
        // Set up a FIS and OIS to grab the object from the file.
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        // We'll grab the object in the file, and then return it
        Object toReturn = ois.readObject();
        
        return toReturn;
    }
}
