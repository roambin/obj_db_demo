package compute.entity.container;

import java.util.LinkedHashMap;

public class UpdateContainer extends Container {
    public String tableName;
    public LinkedHashMap<String, Object> valueMap;
    public WhereContainer whereContainer;
    public UpdateContainer(){
        valueMap = new LinkedHashMap<>();
        whereContainer = new WhereContainer();
    }
}
