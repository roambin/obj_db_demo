package compute.operator;

import compute.Setting;
import compute.entity.LogicalPlan;
import compute.entity.PysicalPlan;
import compute.entity.Result;
import compute.entity.TableInfo;
import mapping.OpObject;
import mapping.entity.Location;

public class InsertOperator {
    public static Result operate(LogicalPlan logicalPlan) {
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, logicalPlan.tableName);
        if(!tableInfo.getTableInfo()){
            return new Result(Setting.TABLEINFO_NOTFOUND_ERROR, false);
        }
        OpObject opObject = Operator.getStorageOpObject(tableInfo.storageName);
        PysicalPlan pysicalPlan = generatePysicalPlan(logicalPlan, tableInfo);
        boolean isSuccess = opObject.insert(pysicalPlan.location, pysicalPlan.content);
        Result result = new Result(isSuccess);
        return result;
    }
    public static PysicalPlan generatePysicalPlan(LogicalPlan logicalPlan, TableInfo tableInfo){
        PysicalPlan pysicalPlan = new PysicalPlan();
        pysicalPlan.storageName = tableInfo.storageName;
        pysicalPlan.location = new Location(logicalPlan.tableName);
        Object keyValue = logicalPlan.insertContainer.valueMap.get(tableInfo.keyName);
        if(keyValue != null)    pysicalPlan.location.key = keyValue.toString();
        pysicalPlan.content.valueMap = logicalPlan.insertContainer.valueMap;
        return pysicalPlan;
    }
}
