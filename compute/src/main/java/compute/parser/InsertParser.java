package compute.parser;

import compute.Setting;
import compute.entity.LogicalPlan;
import compute.entity.TableInfo;
import mapping.utils.SerdeUtils;

import java.util.ArrayList;

public class InsertParser {
    protected static void parse(String command, LogicalPlan logicalPlan){
        command = Parser.cutWord(command);
        String tableName = Parser.readNextWord(command);
        logicalPlan.insertContainer.tableName = tableName;
        logicalPlan.tableName = tableName;
        command = Parser.cutWord(command);
        String[] colNames = null;
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, tableName);
        tableInfo.getTableInfo();
        if(command.charAt(0) == '('){
            String colNamesStr = command.substring(1, command.indexOf(')'));
            colNames = Parser.strToArray(colNamesStr);
            command = command.substring(command.indexOf(')') + 1).trim();
        }else{
            colNames = tableInfo.getTypes();
        }
        if(colNames == null || !command.substring(0, "values".length()).toLowerCase().startsWith("values")){
            Parser.throwException(Parser.exceptionMessage);
        }
        String valuesStr = command.substring(command.indexOf('(') + 1, command.lastIndexOf(')'));
        ArrayList<String> strList = new ArrayList<>();
        valuesStr = Parser.formatString(valuesStr, strList);
        String[] valuesRow = valuesStr.split(",", -1);
        Object[] values = new Object[valuesRow.length];
        for(int i = 0; i < values.length; i++){
            String valueWord = valuesRow[i].trim();
            if(valueWord.equals("#")){
                valueWord = "'" + strList.remove(0) + "'";
            }
            values[i] = SerdeUtils.strToObj(valueWord, tableInfo.getType(colNames[i]));
        }
        for(int i = 0; i < colNames.length; i++){
            logicalPlan.insertContainer.valueMap.put(colNames[i], values[i]);
        }
    }
}
