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

import logging.LoggerSingleton;

import java.io.*;

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
    public static void save(String toSave, String fileName)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
            bw.write(toSave);
            bw.close();
        }
        catch(IOException e)
        {
            LoggerSingleton.logErrorIfEnabled("Unable to save to " + fileName + ". Error: " + e.getMessage());
        }

    }
}
