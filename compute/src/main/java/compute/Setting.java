package compute;

import java.io.File;

public class Setting {
    public static String tableInfoPath = System.getProperty("user.dir") + File.separator + ".." + File.separator + "hub" + File.separator + "table" + File.separator;
    public static String DATABASE = "wormhole";
    public static String TABLEINFO_NOTFOUND_ERROR = "table info not found";
    public static String SQL_GROUPBY_ERROR = "selected columns must be 'group by'";
    public static String DEFAULT_STORAGE = "myfile";
}
