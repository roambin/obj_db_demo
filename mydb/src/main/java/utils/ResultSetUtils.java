package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ResultSetUtils {
    public static HashMap<String, Object> resultSetToMap(ResultSet rs){
        HashMap<String, Object> hashMap = new HashMap<>();
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
