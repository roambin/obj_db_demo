package mapping.utils;

import javassist.*;
import javassist.bytecode.AccessFlag;
import mapping.info.FileInfo;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

public class JavassistUtils {
    public static Class<?> generateClass(String subClassName, HashMap<String, Object> fieldMap){
        try{
            return Class.forName(subClassName);
        }catch (ClassNotFoundException e){

        }
        ClassPool pool = ClassPool.getDefault();
        CtClass ct = pool.makeClass(subClassName);//创建类
        ct.setInterfaces(new CtClass[]{pool.makeInterface("java.io.Serializable")});//让类实现Cloneable接口
        try {
            for(String key: fieldMap.keySet()){
                CtField f= new CtField(pool.get(fieldMap.get(key).getClass().getName()), key, ct);
                f.setModifiers(AccessFlag.PUBLIC);
                ct.addField(f);
            }
            ct.writeFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return ct.toClass();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Object generateObject(String subClassName, HashMap<String, Object> fieldMap){
        Class<?> objClass = generateClass(subClassName, fieldMap);
        try {
            Constructor<?> constructor = objClass.getDeclaredConstructor();
            Object obj = constructor.newInstance();
            for(String key: fieldMap.keySet()){
                setFieldValue(obj, key, fieldMap.get(key));
            }
            return obj;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static boolean dropGenerateClass(String className){
        String pathname = FileInfo.PATHMODULE + className + FileInfo.CLASSFILETYPE;
        File file = new File(pathname);
        if(file.exists()){
            return file.delete();
        }
        return true;
    }
    private static void setFieldValue(Object obj, String fieldName, Object fieldValue) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }
}
