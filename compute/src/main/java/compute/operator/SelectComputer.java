package compute.operator;

import compute.entity.LogicalPlan;
import compute.entity.PysicalResult;
import compute.entity.Result;
import compute.utils.StatisticUtils;
import mapping.entity.Comparer;
import mapping.pattern.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SelectComputer {
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
                isPick = true;
                for(Pattern pattern: patterns){
                    Object value = result.dataMapArr.get(i).get(pattern.colName);
                    isPick = isPick && pattern.isMeet(value);
                    if(!isPick) break;
                }
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
}
