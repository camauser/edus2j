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

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

/**
 * 
 * Purpose: This is a class with a single static method to save objects to
 * files.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class SaveFile
{

    /**
     * 
     * Purpose: Save an object to a file.
     * 
     * @param toSave
     *            - The object to save
     * @param fileName
     *            - The file to save the object to.
     * @throws Exception
     */
    public static void save(Object toSave, String fileName) throws Exception
    {
        // Setting up a FOS and OOS to quickly save the file.
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        // Write out the object to our file
        oos.writeObject(toSave);
        // Save the file, and close the connection.
        oos.close();

    }
}
