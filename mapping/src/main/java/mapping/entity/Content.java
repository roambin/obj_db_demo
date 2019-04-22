package mapping.entity;

import mapping.utils.SerdeUtils;

import java.util.HashMap;
import java.util.Map;

public class Content {
    public HashMap<String, Object> keyValue;
    public Content(){
        keyValue = new HashMap<String, Object>();
    }
    public String getAlterStr(){
        StringBuffer buffer = new StringBuffer();
        for(Map.Entry<String, Object> entry: keyValue.entrySet()){
            buffer.append(entry.getKey()).append(" = ").append(SerdeUtils.objToStr(entry.getValue())).append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        return new String(buffer);
    }
}
