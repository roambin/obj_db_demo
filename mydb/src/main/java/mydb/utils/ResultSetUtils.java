package mydb.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ResultSetUtils {
    public static LinkedHashMap<String, Object> resultSetToMap(ResultSet rs){
        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();
        try{
            for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                hashMap.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return hashMap;
    }
}
