package myfile.operator;

import myfile.info.FileInfo;
import myfile.utils.SerdeUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public static boolean truncate(String className){
        String pathname = FileInfo.getSerdePath(className);
        File file = new File(pathname);
        if(file.exists()){
            try {
                FileUtils.deleteDirectory(file);
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return true;
    }
    public static FileIterator select(String className){
        String path = FileInfo.getSerdePath(className);
        final ArrayList<File> files = new ArrayList<>();
        File superFile = new File(path);
        if(superFile.exists()){
            for(File file: superFile.listFiles()){
                if(!file.getName().startsWith(".")){
                    files.add(file);
                }
            }
        }
        return new FileIterator(files);
    }
/*    public static String[] listClassNames( ){
        String path = FileInfo.PATHMODULE;
        File file = new File(path);
        return file.list();
    }*/
}
