package operator;

import info.FileInfo;
import utils.SerdeUtils;

import java.io.File;
import java.util.Iterator;

public class FileOperator{
    public Object read(String className, String objName){
        String path = FileInfo.getSerdePath(className) + objName;
        return SerdeUtils.deserialize(path);
    }
    public static boolean write(String objName, Object obj){
        String superPath = FileInfo.getSerdePath(obj.getClass());
        return SerdeUtils.serialize(superPath, objName, obj);
    }
    public static boolean update(String objName, Object obj){
        String className = obj.getClass().getCanonicalName();
        delete(className, objName);
        write(objName, obj);
        return true;
    }
    public static boolean delete(String className, String objName){
        return SerdeUtils.delete(FileInfo.getSerdePath(className), objName);
    }
    public static Iterator<Object> select(String className){
        String path = FileInfo.getSerdePath(className);
        final File[] files = new File(path).listFiles();
        return new Iterator<Object>() {
            int index = -1;
            public boolean hasNext() {
                if(files == null){
                    return false;
                }
                return index + 1 < files.length;
            }
            public Object next() {
                if(files == null || !hasNext()){
                    throw  new UnsupportedOperationException("next element not exists");
                }
                index++;
                return SerdeUtils.deserialize(files[index]);
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
