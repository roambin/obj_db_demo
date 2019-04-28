package mapping.entity;

import mapping.utils.SerdeUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Content {
    public LinkedHashMap<String, Object> valueMap = new LinkedHashMap<>();
    public Content(){

    }
    public String getAlterStr(){
        StringBuffer buffer = new StringBuffer();
        for(Map.Entry<String, Object> entry: valueMap.entrySet()){
            buffer.append(entry.getKey()).append(" = ").append(SerdeUtils.objToStr(entry.getValue())).append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        return new String(buffer);
    }
}
