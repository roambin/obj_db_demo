package myfile.utils;

import myfile.utils.entity.MyfileObjectInputStream;

import java.io.*;

public class SerdeUtils {

    public static boolean mkdirs(String path){
        File dir = new File(path);
        if(!dir.exists() || dir.isFile()){
            return dir.mkdirs();
        }
        return true;
    }
    public static boolean serialize(String superPath, String name, Object obj){
        mkdirs(superPath);
        String path = superPath + name;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static Object deserialize(File file){
        return deserialize(file.getPath());
    }
    public static Object deserialize(String path){
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new MyfileObjectInputStream(fis);
            Object obj = ois.readObject();
            ois.close();
            return obj;
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public static boolean delete(String superPath, String name){
        boolean isDelete = true;
        File file = new File(superPath + name);
        if(file.exists() && file.isFile()){
            isDelete = file.delete();
            rmdirIfEmpty(superPath);
        }
        return isDelete;
    }
    public static boolean rmdirIfEmpty(String path){
        File file = new File(path);
        if(file.exists() && file.isDirectory() && file.list().length == 0){
            return file.delete();
        }
        return true;
    }
}
