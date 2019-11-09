package de.jzbor.hgvinfo;

import java.io.*;

public class Utils {
    public static void saveObject(File dir, String filename, Object object) throws IOException {
        // Save a serializable object
        if (!(object instanceof Serializable))
            return;
        File file = new File(dir, filename);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object openObject(File dir, String filename) throws IOException, ClassNotFoundException {
        // Open a serializable object
        File file = new File(dir, filename);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        ois.close();
        fis.close();
        return object;
    }
}
