package mapping.pattern;

import mapping.utils.SerdeUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class InPattern extends Pattern{
    ArrayList<Object> values;
    boolean isNot;
    public InPattern(String colName, boolean isNot, Object... values){
        super(colName);
        this.values = new ArrayList<>(Arrays.asList(values));
        this.isNot = isNot;
    }

    @Override
    public String toString() {
        if(values.size() == 0)  return "";
        StringBuffer buffer = new StringBuffer();
        for(Object value: values){
            buffer.append(SerdeUtils.objToStr(value)).append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        return colName + " in (" + new String(buffer) + ")";
    }

    @Override
    public boolean isMeet(Object value) {
        return isNot != values.contains(value);
    }
}
