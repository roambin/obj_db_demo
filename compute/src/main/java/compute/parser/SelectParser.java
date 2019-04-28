package compute.parser;

import compute.entity.LogicalPlan;
import compute.entity.container.GroupByContainer;
import compute.entity.container.OrderByContainer;
import compute.entity.container.StatisticContainer;

import java.util.ArrayList;

public class SelectParser {
    protected static void parse(String command, LogicalPlan logicalPlan){
        String[] colNamesSelect = Parser.strToArray(command.substring(0, command.indexOf("from")));
        command = command.substring(command.indexOf("from") + 4).trim();
        String tableName = Parser.readNextWord(command);
        logicalPlan.selectContainer.tableName = tableName;
        logicalPlan.tableName = tableName;
        command = command.substring(tableName.length()).trim();
        logicalPlan.selectContainer.colNames = colNamesSelect;
        //compute
        for(int i = 0; i < colNamesSelect.length; i++){
            String statisticType = Parser.readNextWord(colNamesSelect[i]);
            if(!statisticType.equals(colNamesSelect[i])){
                String colName = Parser.cutWord(colNamesSelect[i]).trim();
                colName = colName.substring(1, colName.length() - 1).trim();
                //colNamesSelect[i] = colName;
                logicalPlan.selectContainer.colNameParsedSet.add(colName);
                logicalPlan.selectContainer.statisticContainers.put(colNamesSelect[i], new StatisticContainer(colName, statisticType.toLowerCase(), i));
            }else{
                logicalPlan.selectContainer.colNameParsedSet.add(colNamesSelect[i]);
            }
        }
        //where, group by, order by
        if(command.length() == 0)   return;
        ArrayList<String> strList = new ArrayList<>();
        command = Parser.formatString(command, strList);
        int indexOrder = command.indexOf("order by");
        int indexGroup = command.indexOf("group by");
        if(command.substring(0, 5).toLowerCase().equals("where")){
            command = command.trim();
            int indexSplit;
            if(indexGroup != -1 && indexOrder != -1){
                indexSplit = Math.min(indexGroup, indexOrder);
            }else{
                indexSplit = Math.max(indexGroup, indexOrder);
            }
            String whereCommand = indexSplit == -1 ? command.substring(5).trim() : command.substring(5, indexSplit).trim();
            logicalPlan.selectContainer.whereContainer = WhereParser.whereCommandToContainer(whereCommand, tableName, strList);
            if(indexSplit != -1)    command = command.substring(indexSplit).trim();
        }
        if(indexGroup != -1){
            indexOrder = command.indexOf("order by");
            String groupCommand = indexOrder == -1 ? command : command.substring(0, indexOrder);
            groupCommand = groupCommand.substring("group by".length()).trim();

            logicalPlan.selectContainer.groupByContainer = new GroupByContainer(Parser.strToArray(groupCommand));
            if(indexOrder != -1)    command = command.substring(indexOrder).trim();
        }
        if(indexOrder != -1){
            String orderCommand = command.trim().substring("order by".length());
            String[] words = Parser.strToArray(orderCommand);
            String colNamesOrder[] = new String[words.length];
            Boolean[] isAscArry = new Boolean[words.length];
            for(int i = 0; i < words.length; i++){
                colNamesOrder[i] = Parser.readNextWord(words[i]);
                String sortType = Parser.cutWord(words[i]);
                isAscArry[i] = !sortType.toLowerCase().equals("desc");
            }
            logicalPlan.selectContainer.orderByContainer = new OrderByContainer(colNamesOrder, isAscArry);
        }
    }
}
