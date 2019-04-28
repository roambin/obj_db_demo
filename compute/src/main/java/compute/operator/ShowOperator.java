package compute.operator;

import compute.entity.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ShowOperator {
    public static Result operate(LogicalPlan logicalPlan) {
        ArrayList<LinkedHashMap<String, Object>> dataMapArr = new ArrayList<>();
        String[] tableNames = TableInfo.listTables();
        for(String tableName: tableNames){
            if(!tableName.startsWith(".")){
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("table", tableName);
                dataMapArr.add(map);
            }
        }
        Result result = new Result(dataMapArr, new String[]{"table"});
        return result;
    }
}
