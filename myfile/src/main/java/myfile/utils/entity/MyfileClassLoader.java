package myfile.utils.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyfileClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) {
        String path = name.replace(".","/") + ".class";
        byte[] cLassBytes = null;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            cLassBytes = new byte[fis.available()];
            fis.read(cLassBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class clazz = defineClass(name, cLassBytes, 0, cLassBytes.length);
        return clazz;
    }
}
