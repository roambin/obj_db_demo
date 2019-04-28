package myfile.info;

import java.io.File;

public class FileInfo {
    public static String PATHDATA = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    public static String PATHMODULE = System.getProperty("user.dir") + File.separator;
    public static String CLASSFILETYPE = ".class";
    public static String getSerdePath(Class<?> objClass){
        return PATHDATA + objClass.getCanonicalName() + File.separator;
    }
    public static String getSerdePath(String className){
        return PATHDATA + className + File.separator;
    }
}
