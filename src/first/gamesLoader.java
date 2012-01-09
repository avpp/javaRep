/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package first;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Alexey
 */
public class gamesLoader extends ClassLoader{
    public gamesLoader(ClassLoader parrent)
    {
        super(parrent);
    }
    public Class findClass(String name, File f) throws FileNotFoundException, IOException
    {
        if (f.length() > Integer.MAX_VALUE)
            throw new FileNotFoundException("File to large");
        InputStream file = new FileInputStream(f);
        byte memory[] = new byte[(int)f.length()];
        file.read(memory);
        return defineClass(name, memory, 0, memory.length);
    }
}
