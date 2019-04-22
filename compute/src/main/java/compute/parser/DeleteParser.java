package compute.parser;

import compute.entity.LogicalPlan;
import java.util.ArrayList;

public class DeleteParser {
    protected static void parseDelete(String command, LogicalPlan logicalPlan){
        if(!Parser.readNextWord(command).toLowerCase().equals("from")){
            Parser.throwException(Parser.exceptionMessage);
        }
        command = Parser.cutWord(command);
        String tableName = Parser.readNextWord(command);
        logicalPlan.deleteContainer.tableName = tableName;
        command = Parser.cutWord(command);
        ArrayList<String> strList = new ArrayList<>();
        command = Parser.formatString(command, strList);
        int indexWhere = command.indexOf("where");
        String whereCommand = command.substring(indexWhere + "where".length()).trim();
        if(indexWhere != -1){
            logicalPlan.updateContainer.whereContainer = WhereParser.whereCommandToContainer(whereCommand, tableName, strList);
        }
    }
}
