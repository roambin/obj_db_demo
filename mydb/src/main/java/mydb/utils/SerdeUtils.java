package mydb.utils;

public class SerdeUtils {
    public static String objToStringValue(Object obj){
        return "'" + obj.toString() + "'";
    }
    public static String objToStringKey(Object obj){
        return obj.toString();
    }
}
