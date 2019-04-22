package compute.parser;

import compute.Setting;
import compute.entity.LogicalPlan;
import compute.entity.TableInfo;
import mapping.utils.SerdeUtils;

import java.util.ArrayList;

public class UpdateParser {
    protected static void parseUpdate(String command, LogicalPlan logicalPlan){
        String tableName = Parser.readNextWord(command);
        logicalPlan.updateContainer.tableName = tableName;
        command = Parser.cutWord(command);
        if(!Parser.readNextWord(command).toLowerCase().equals("set")){
            Parser.throwException(Parser.exceptionMessage);
        }
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, tableName);
        tableInfo.getTableInfo();
        command = Parser.cutWord(command);
        ArrayList<String> strList = new ArrayList<>();
        command = Parser.formatString(command, strList);
        String entryStr = command.trim();
        int indexWhere = command.indexOf("where");
        String whereCommand = null;
        if(indexWhere != -1){
            entryStr = command.substring(0, indexWhere).trim();
            whereCommand = command.substring(indexWhere + "where".length()).trim();
        }
        String[] entriesRow = Parser.strToArray(entryStr);
        for(String entry: entriesRow){
            String colName = entry.split("=")[0].trim();
            String valueWord = entry.split("=")[1].trim();
            if(valueWord.equals("#")){
                valueWord = "'" + strList.remove(0) + "'";
            }
            Object value = SerdeUtils.strToObj(valueWord, tableInfo.getType(colName));
            logicalPlan.updateContainer.valueMap.put(colName, value);
        }
        if(indexWhere != -1){
            logicalPlan.updateContainer.whereContainer = WhereParser.whereCommandToContainer(whereCommand, tableName, strList);
        }
    }
}
