package compute.entity.container;

import java.util.LinkedHashMap;

public class InsertContainer extends Container {
    public String tableName;
    public LinkedHashMap<String, Object> valueMap;
    public InsertContainer(){
        valueMap = new LinkedHashMap<>();
    }
}