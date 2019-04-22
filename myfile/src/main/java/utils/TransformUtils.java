package utils;

import mapping.entity.Content;
import mapping.entity.Location;
import java.lang.reflect.Field;

public class TransformUtils {
    public static Location getLocation(Object obj){
        Location location = new Location(obj.getClass().getCanonicalName());
        return location;
    }
    public static Content getContent(Object obj){
        Content content = new Content();
        for(Field field: obj.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                content.keyValue.put(field.getName(), field.get(obj));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return content;
    }
    public static Object toObject(Location location, Content content){
        Object obj = null;
        if(content.keyValue != null){
            obj = JavassistUtils.generateObject(location.tableName, content.keyValue);
        }
        return obj;
    }
}
