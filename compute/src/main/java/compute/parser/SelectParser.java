package compute.parser;

import compute.Setting;
import compute.entity.LogicalPlan;
import compute.entity.TableInfo;
import compute.entity.container.GroupByContainer;
import compute.entity.container.OrderByContainer;
import compute.entity.container.WhereContainer;
import mapping.pattern.BetweenPattern;
import mapping.pattern.ComparePattern;
import mapping.pattern.IsNullPattern;
import mapping.pattern.Pattern;
import mapping.utils.SerdeUtils;

import java.util.ArrayList;

public class SelectParser {
    protected static void parseSelect(String command, LogicalPlan logicalPlan){
        String[] colNames = Parser.strToArray(command.substring(0, command.indexOf("from")));
        logicalPlan.selectContainer.colNames = colNames;
        command = command.substring(command.indexOf("from") + 4).trim();
        String tableName = command.substring(0, command.indexOf(' '));
        logicalPlan.selectContainer.tableName = tableName;
        command = command.substring(tableName.length()).trim();
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
            colNames = new String[words.length];
            boolean[] isAscArry = new boolean[words.length];
            for(int i = 0; i < words.length; i++){
                colNames[i] = words[i].substring(0, words[i].indexOf(' '));
                isAscArry[i] = words[i].substring(words[i].lastIndexOf(' ') + 1).toLowerCase().equals("asc");
            }
            logicalPlan.selectContainer.orderByContainer = new OrderByContainer(colNames, isAscArry);
        }
    }
}
