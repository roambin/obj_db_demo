package compute.entity.container;

import java.util.LinkedHashMap;

public class CreateContainer extends Container{
    public String tableName;
    public String primaryKey;
    public String storageName;
    public LinkedHashMap<String, String> typeMap;
    public CreateContainer(){
        typeMap = new LinkedHashMap<>();
    }
}
