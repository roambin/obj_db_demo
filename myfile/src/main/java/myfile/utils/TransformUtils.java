package myfile.utils;

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
                content.valueMap.put(field.getName(), field.get(obj));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return content;
    }
    public static Object toObject(Location location, Content content){
        Object obj = null;
        if(content.valueMap != null){
            obj = JavassistUtils.generateObject(location.tableName, content.valueMap);
        }
        return obj;
    }
}
