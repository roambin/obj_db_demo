package compute.operator;

import compute.Setting;
import compute.entity.LogicalPlan;
import compute.entity.PysicalPlan;
import compute.entity.Result;
import compute.entity.TableInfo;
import mapping.OpObject;
import mapping.entity.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateOperator {
    public static Result operate(LogicalPlan logicalPlan) {
        storeTableInfo(logicalPlan);
        OpObject opObject = Operator.getStorageOpObject(logicalPlan.createContainer.storageName);
        PysicalPlan pysicalPlan = generatePysicalPlan(logicalPlan);
        boolean isSuccess = opObject.create(pysicalPlan.location);
        Result result = new Result(isSuccess);
        return result;
    }

    public static PysicalPlan generatePysicalPlan(LogicalPlan logicalPlan) {
        PysicalPlan pysicalPlan = new PysicalPlan();
        pysicalPlan.location = new Location(logicalPlan.tableName);
        pysicalPlan.location.colNames = new ArrayList<>(logicalPlan.createContainer.typeMap.keySet());
        pysicalPlan.location.colTypes = new ArrayList<>(logicalPlan.createContainer.typeMap.values());
        return pysicalPlan;
    }
    public static void storeTableInfo(LogicalPlan logicalPlan){
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, logicalPlan.tableName);
        tableInfo.colTypeMap = logicalPlan.createContainer.typeMap;
        tableInfo.keyName = logicalPlan.createContainer.primaryKey;
        tableInfo.storeTableInfo(logicalPlan.createContainer.storageName);
    }
}
