package compute.operator;

import compute.Setting;
import compute.entity.*;
import mapping.OpObject;
import mapping.entity.Location;
import mapping.entity.VirtualColumn;


public class UpdateOperator {
    public static Result operate(LogicalPlan logicalPlan) {
        logicalPlan.updateContainer.whereContainer.generateCondition(true);
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, logicalPlan.tableName);
        if(!tableInfo.getTableInfo()){
            return new Result(Setting.TABLEINFO_NOTFOUND_ERROR, false);
        }
        OpObject opObject = Operator.getStorageOpObject(tableInfo.storageName);
        PysicalPlan pysicalPlan = generatePysicalPlan(logicalPlan, tableInfo);
        Result result;
        boolean isSuccess = true;
        if(opObject.canFilterPushDown){
            pysicalPlan.location.condition = logicalPlan.updateContainer.whereContainer.condition;
            isSuccess = opObject.update(pysicalPlan.location, pysicalPlan.content);
        } else{
            LogicalPlan rewriteLogicalPlan = rewriteLogicalPlan(logicalPlan);
            isSuccess = rewriteRun(rewriteLogicalPlan, pysicalPlan, opObject);
        }
        result = new Result(isSuccess);
        return result;
    }
    public static PysicalPlan generatePysicalPlan(LogicalPlan logicalPlan, TableInfo tableInfo){
        PysicalPlan pysicalPlan = new PysicalPlan();
        pysicalPlan.storageName = tableInfo.storageName;
        pysicalPlan.location = new Location(logicalPlan.tableName);
        pysicalPlan.content.valueMap = logicalPlan.updateContainer.valueMap;
        return pysicalPlan;
    }
    public static LogicalPlan rewriteLogicalPlan(LogicalPlan logicalPlan){
        LogicalPlan rewriteLogicalPlan = new LogicalPlan();
        rewriteLogicalPlan.type = "select";
        rewriteLogicalPlan.tableName = logicalPlan.tableName;
        rewriteLogicalPlan.selectContainer.colNames = new String[]{VirtualColumn.colIndex};
        rewriteLogicalPlan.selectContainer.whereContainer.condition = logicalPlan.updateContainer.whereContainer.condition;
        return rewriteLogicalPlan;
    }
    public static boolean rewriteRun(LogicalPlan rewriteLogicalPlan, PysicalPlan pysicalPlan, OpObject opObject){
        boolean isSuccess = true;
        Result rewriteResult = SelectOperator.operate(rewriteLogicalPlan);
        String[] colIndexs = new String[rewriteResult.dataMapArr.size()];
        for(int i = 0; i < colIndexs.length; i++){
            colIndexs[i] = rewriteResult.dataMapArr.get(i).get(VirtualColumn.colIndex).toString();
        }
        for(int i = 0; i < colIndexs.length; i++){
            pysicalPlan.location.key = colIndexs[i];
            isSuccess = isSuccess && opObject.update(pysicalPlan.location, pysicalPlan.content);
        }
        return isSuccess;
    }
}
