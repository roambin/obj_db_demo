package mapping.utils;

public class SerdeUtils {
    public static String objToStr(Object obj){
        return "'" + obj.toString() + "'";
    }
    public static Object strToObj(String str, String type){
        int indexSplit = type.indexOf('(');
        if(indexSplit != -1){
            type = type.substring(0, type.indexOf('('));
        }
        switch (type){
            case "bool":
            case "boolean":
                return Boolean.parseBoolean(str);
            case "int":
                return Integer.parseInt(str);
            case "float":
                return Float.parseFloat(str);
            case "double":
                return Double.parseDouble(str);
            case "char":
            case "varchar":
            case "string":
                return str.substring(1, str.length() - 1);
            default:
                 throw new UnsupportedOperationException("unsupport type: " + type);
        }
    }
}
