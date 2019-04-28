package compute.operator;

import compute.Setting;
import compute.entity.LogicalPlan;
import compute.entity.PysicalPlan;
import compute.entity.Result;
import compute.entity.TableInfo;
import mapping.OpObject;
import mapping.entity.Location;

public class TruncateOperator {
    public static Result operate(LogicalPlan logicalPlan) {
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, logicalPlan.tableName);
        if(!tableInfo.getTableInfo()){
            return new Result(Setting.TABLEINFO_NOTFOUND_ERROR, false);
        }
        OpObject opObject = Operator.getStorageOpObject(tableInfo.storageName);
        PysicalPlan pysicalPlan = generatePysicalPlan(logicalPlan);
        boolean isSuccess = opObject.truncate(pysicalPlan.location);
        Result result = new Result(isSuccess);
        return result;
    }

    public static PysicalPlan generatePysicalPlan(LogicalPlan logicalPlan) {
        PysicalPlan pysicalPlan = new PysicalPlan();
        pysicalPlan.location = new Location(logicalPlan.tableName);
        return pysicalPlan;
    }
}
