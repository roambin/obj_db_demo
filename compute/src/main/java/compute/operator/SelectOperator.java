package compute.operator;

import compute.Setting;
import compute.entity.*;
import compute.utils.SetUtils;
import mapping.OpObject;
import mapping.entity.Location;
import mapping.operator.IO;

import java.util.*;

public class SelectOperator {
    public static Result operate(LogicalPlan logicalPlan){
        //get table info
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, logicalPlan.tableName);
        if(!tableInfo.getTableInfo()){
            return new Result(Setting.TABLEINFO_NOTFOUND_ERROR, false);
        }
        //replace *
        if(logicalPlan.selectContainer.colNames[0].equals("*")){
            logicalPlan.selectContainer.colNames = tableInfo.colTypeMap.keySet().toArray(new String[0]);
            logicalPlan.selectContainer.colNameParsedSet = new HashSet<>(Arrays.asList(logicalPlan.selectContainer.colNames));
        }
        //check sql
        String errorMessage = check(logicalPlan);
        if(errorMessage != null){
            return new Result(errorMessage, false);
        }
        //operate
        PysicalPlan pysicalPlan = generatePysicalPlan(logicalPlan, tableInfo);
        IO io = Operator.getStorageIO(pysicalPlan.storageName);
        if(io != null)  io.open();
        PysicalResult pysicalResult = pysicalOperate(pysicalPlan, io);
        Result result = SelectComputer.compute(logicalPlan, pysicalResult);
        deleteVirtualColumn(result);
        return result;
    }
    public static String check(LogicalPlan logicalPlan){
        if(logicalPlan.selectContainer.groupByContainer != null){
            HashSet<String> colNameSet = logicalPlan.selectContainer.colNameParsedSet;
            if(logicalPlan.selectContainer.orderByContainer != null){
                colNameSet.addAll(Arrays.asList(logicalPlan.selectContainer.orderByContainer.colNames));
            }
            HashSet<String> colNameGroupSet = new HashSet<>(Arrays.asList(logicalPlan.selectContainer.groupByContainer.colNames));
            colNameGroupSet.addAll(logicalPlan.selectContainer.getStatisticColumnNameSet());
            for(String colName: colNameSet){
                if (!colNameGroupSet.contains(colName)){
                    return Setting.SQL_GROUPBY_ERROR;
                }
            }
        }
        return null;
    }
    public static PysicalPlan generatePysicalPlan(LogicalPlan logicalPlan, TableInfo tableInfo){
        PysicalPlan pysicalPlan = new PysicalPlan();
        pysicalPlan.storageName = tableInfo.storageName;
        pysicalPlan.location = new Location(logicalPlan.tableName);
        pysicalPlan.location.colNames = new ArrayList<>(Arrays.asList(logicalPlan.selectContainer.colNames));
        pysicalPlan.location.colTypes = tableInfo.getTypes(logicalPlan.selectContainer.colNames);
        if(logicalPlan.selectContainer.whereContainer != null){
            pysicalPlan.location.condition = logicalPlan.selectContainer.whereContainer.generateCondition(false);
        }
        if(logicalPlan.selectContainer.groupByContainer != null){
            pysicalPlan.location.groupColNames = new ArrayList<>(Arrays.asList(logicalPlan.selectContainer.groupByContainer.colNames));
        }
        if(logicalPlan.selectContainer.orderByContainer != null){
            pysicalPlan.location.orderColNames = new ArrayList<>(Arrays.asList(logicalPlan.selectContainer.orderByContainer.colNames));
            pysicalPlan.location.orderIsAsc = new ArrayList<>(Arrays.asList(logicalPlan.selectContainer.orderByContainer.isAsc));
        }
        //rewrite pysicalPlan's colNames
        OpObject opObject = Operator.getStorageOpObject(pysicalPlan.storageName);
        HashSet<String> addColumns = new HashSet<>();
        if(logicalPlan.selectContainer.whereContainer != null && !opObject.canFilterPushDown){
            addColumns.addAll(logicalPlan.selectContainer.whereContainer.colNameSet);
        }
        if(logicalPlan.selectContainer.groupByContainer != null && !opObject.canDimensionPushDown){
            addColumns.addAll(SetUtils.ArrayToSet(logicalPlan.selectContainer.groupByContainer.colNames));
        }
        if(logicalPlan.selectContainer.orderByContainer != null && !opObject.canOrderbyPushDown){
            addColumns.addAll(SetUtils.ArrayToSet(logicalPlan.selectContainer.orderByContainer.colNames));
        }
        if(logicalPlan.selectContainer.statisticContainers != null && !opObject.canDimensionPushDown){
            pysicalPlan.location.colNames.removeAll(logicalPlan.selectContainer.statisticContainers.keySet());
            addColumns.addAll(logicalPlan.selectContainer.getStatisticColumnNameSet());
        }
        addColumns.removeAll(pysicalPlan.location.colNames);
        pysicalPlan.location.colNames.addAll(addColumns);

        return pysicalPlan;
    }
    public static PysicalResult pysicalOperate(PysicalPlan pysicalPlan, IO io){
        PysicalResult pysicalResult = new PysicalResult();
        OpObject opObject = Operator.getStorageOpObject(pysicalPlan.storageName);
        pysicalResult.columnIter = opObject.select(pysicalPlan.location, io);
        pysicalResult.isFilterPushDown = pysicalPlan.location.isFilterPushDown;
        pysicalResult.isDimensionPushDown = pysicalPlan.location.isDimensionPushDown;
        pysicalResult.isOrderbyPushDown = pysicalPlan.location.isOrderbyPushDown;
        return pysicalResult;
    }

    public static void deleteVirtualColumn(Result result){
        ArrayList<LinkedHashMap<String, Object>> dataMapArrNew = new ArrayList<>();
        for(LinkedHashMap<String, Object> map: result.dataMapArr){
            LinkedHashMap<String, Object> mapNew = new LinkedHashMap<>();
            for(String key: result.colNames){
                mapNew.put(key, map.remove(key));
            }
            dataMapArrNew.add(mapNew);
        }
        result.dataMapArr = dataMapArrNew;
    }
}
