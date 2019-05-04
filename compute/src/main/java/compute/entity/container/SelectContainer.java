package compute.entity.container;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class SelectContainer extends Container {
    public String tableName;
    public String[] colNames;
    public HashSet<String> colNameParsedSet;
    public WhereContainer whereContainer;
    public GroupByContainer groupByContainer;
    public OrderByContainer orderByContainer;
    public LinkedHashMap<String, StatisticContainer> statisticContainers;
    public SelectContainer(){
        whereContainer = new WhereContainer();
        statisticContainers = new LinkedHashMap<>();
        colNameParsedSet = new HashSet<>();
    }
    public HashSet<String> getStatisticColumnNameSet(){
        HashSet<String> hashSet = new HashSet<>();
        for(Entry<String, StatisticContainer> entry: statisticContainers.entrySet()){
            hashSet.add(entry.getValue().colname);
        }
        return hashSet;
    }
}
