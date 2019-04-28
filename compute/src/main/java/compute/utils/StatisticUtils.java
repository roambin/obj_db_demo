package compute.utils;

import java.util.ArrayList;

public class StatisticUtils {
    public static Object plus(Object v1, Object v2){
        if(v1 == null && v2 == null)    return null;
        if(v1 == null || v2 == null){
            return v1 == null ? v2 : v1;
        }
        if(!v1.getClass().equals(v2.getClass())){
            throw new UnsupportedOperationException("class not match ( v1: " + v1.getClass() + ", v2: " + v2.getClass() + ")");
        }
        if(v1.getClass().equals(Integer.class))  return (Integer)v1 + (Integer)v2;
        else if(v1.getClass().equals(Float.class))  return (Float)v1 + (Float)v2;
        else if(v1.getClass().equals(Double.class))  return (Double)v1 + (Double)v2;
        else    throw new UnsupportedOperationException("Unsupport type: " + v1.getClass());
    }
    public static Object plus(ArrayList<Object> vs){
        Object sum = null;
        for(Object v: vs){
            sum = plus(sum, v);
        }
        return sum;
    }
    public static Object divide(Object v, int count){
        if(v.getClass().equals(Integer.class))  return (float)((Integer)v).intValue()/count;
        else if(v.getClass().equals(Float.class))  return (Float)v/count;
        else if(v.getClass().equals(Double.class))  return (Double)v/count;
        else    throw new UnsupportedOperationException("Unsupport type: " + v.getClass());
    }
    public static boolean isStatistic(String colName){
        return colName.contains("(");
    }
}
