package compute.operator;

import compute.Setting;
import compute.entity.*;
import compute.utils.StatisticUtils;
import compute.utils.SetUtils;
import mapping.OpObject;
import mapping.entity.Comparer;
import mapping.entity.Location;
import mapping.operator.IO;
import mapping.pattern.Pattern;
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
        Result result = compute(logicalPlan, pysicalResult);
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
    public static Result compute(LogicalPlan logicalPlan, PysicalResult pysicalResult){
        Result result = new Result(pysicalResult.columnIter, logicalPlan.selectContainer.colNames);
        doWhere(logicalPlan, pysicalResult, result);
        doGroupby(logicalPlan, pysicalResult, result);
        doOrderby(logicalPlan, pysicalResult, result);
        return result;
    }
    public static void doWhere(LogicalPlan logicalPlan, PysicalResult pysicalResult, Result result){
        if(pysicalResult.isFilterPushDown)  return;
        ArrayList<ArrayList<Pattern>> condition = logicalPlan.selectContainer.whereContainer.generateCondition(false);
        for(int i = 0; i < result.dataMapArr.size(); i++){
            boolean isPick = true;
            for(ArrayList<Pattern> patterns: condition){
                for(Pattern pattern: patterns){
                    Object value = result.dataMapArr.get(i).get(pattern.colName);
                    isPick = isPick && pattern.isMeet(value);
                    if(!isPick) break;
                }
                isPick = isPick || isPick;
                if (isPick) break;
            }
            if(!isPick){
                result.dataMapArr.remove(i--);
            }
        }
    }
    public static void doGroupby(LogicalPlan logicalPlan, PysicalResult pysicalResult, Result result){
        if(pysicalResult.isDimensionPushDown) return;
        if(logicalPlan.selectContainer.groupByContainer == null){
            doStatistic(logicalPlan, result.dataMapArr);
            return;
        }else{
            //generate group
            HashMap<ArrayList<Object>, ArrayList<LinkedHashMap<String, Object>>> group = new HashMap<>();
            for(LinkedHashMap<String, Object> map: result.dataMapArr){
                ArrayList<Object> key = new ArrayList<>();
                for(String colName: logicalPlan.selectContainer.groupByContainer.colNames){
                    key.add(map.get(colName));
                }
                if(group.get(key) == null){
                    ArrayList<LinkedHashMap<String, Object>> dataMapArr = new ArrayList<>();
                    dataMapArr.add(map);
                    group.put(key, dataMapArr);
                }else{
                    group.get(key).add(map);
                }
            }
            //do statistic for each dataMapArr and merge
            result.dataMapArr.clear();
            for(ArrayList<LinkedHashMap<String, Object>> dataMapArr: group.values()){
                doStatistic(logicalPlan, dataMapArr);
                result.dataMapArr.addAll(dataMapArr);
            }
        }
    }
    public static void doStatistic(LogicalPlan logicalPlan, ArrayList<LinkedHashMap<String, Object>> dataMapArr){
        if(dataMapArr.size() == 0)  return;
        LinkedHashMap<String, Object> statisticMap = new LinkedHashMap<>();
        for(int i = 0; i < logicalPlan.selectContainer.colNames.length; i++){
            String colName = logicalPlan.selectContainer.colNames[i];
            if(!StatisticUtils.isStatistic(colName)){
                statisticMap.put(colName, dataMapArr.get(0).get(colName));
            }else{
                Object statisticValue = null;
                ArrayList<Comparer> comparers = new ArrayList<>();
                for(int j = 0; j < dataMapArr.size(); j++){
                    String statisticColumnName = logicalPlan.selectContainer.statisticContainers.get(colName).colname;
                    comparers.add(new Comparer(dataMapArr.get(j).get(statisticColumnName), j));
                }
                switch (logicalPlan.selectContainer.statisticContainers.get(colName).statisticType){
                    case "count":
                        statisticValue = dataMapArr.size();
                        break;
                    case "min":
                        statisticValue = Collections.min(comparers).value;
                        break;
                    case "max":
                        statisticValue = Collections.max(comparers).value;
                        break;
                    case "sum":
                        statisticValue = StatisticUtils.plus(Comparer.getValueList(comparers));
                        break;
                    case "avg":
                        Object sum = StatisticUtils.plus(Comparer.getValueList(comparers));
                        int count = dataMapArr.size();
                        statisticValue = StatisticUtils.divide(sum, count);
                        break;
                }
                statisticMap.put(colName, statisticValue);
            }
        }
        dataMapArr.clear();
        dataMapArr.add(statisticMap);
    }
    public static void doOrderby(LogicalPlan logicalPlan, PysicalResult pysicalResult, Result result){
        if(pysicalResult.isOrderbyPushDown) return;
        if(logicalPlan.selectContainer.orderByContainer == null)    return;
        for(int i = logicalPlan.selectContainer.orderByContainer.colNames.length - 1; i >= 0; i--){
            String colName = logicalPlan.selectContainer.orderByContainer.colNames[i];
            boolean isAsc = logicalPlan.selectContainer.orderByContainer.isAsc[i];
            sortByColumn(result, colName, isAsc);
        }
    }
    protected static void sortByColumn(Result result, String colName, boolean isAsc){
        ArrayList<Comparer> comparers = new ArrayList<>();
        for(int i = 0; i < result.dataMapArr.size(); i++){
            comparers.add(new Comparer(result.dataMapArr.get(i).get(colName), i));
        }
        if(isAsc) Collections.sort(comparers);
        else Collections.sort(comparers, Collections.reverseOrder());
        ArrayList<LinkedHashMap<String, Object>> dataMapArr = new ArrayList<>();
        for(int i = 0; i < comparers.size(); i++){
            dataMapArr.add(result.dataMapArr.get(comparers.get(i).index));
        }
        result.dataMapArr = dataMapArr;
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
