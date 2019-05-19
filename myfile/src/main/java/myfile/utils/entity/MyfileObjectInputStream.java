package myfile.utils.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class MyfileObjectInputStream extends ObjectInputStream{
    public MyfileObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        try {
            return super.resolveClass(desc);
        } catch (ClassNotFoundException ex) {
            ClassLoader cl = new MyfileClassLoader();
            return cl.loadClass(name);
        }
    }
}
