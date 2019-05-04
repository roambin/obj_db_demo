package myfile.info;

import java.io.File;

public class FileInfo {
    public static String SUPERPATH = System.getProperty("user.dir") + File.separator + ".." + File.separator + "hub";
    public static String PATHDATA = SUPERPATH + File.separator + "data" + File.separator;
    public static String getSerdePath(Class<?> objClass){
        return PATHDATA + objClass.getCanonicalName() + File.separator;
    }
    public static String getSerdePath(String className){
        return PATHDATA + className + File.separator;
    }
}
