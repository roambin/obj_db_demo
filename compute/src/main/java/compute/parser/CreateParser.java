package compute.parser;

import compute.entity.LogicalPlan;

import java.util.ArrayList;

public class CreateParser {
    protected static void parseCreate(String command, LogicalPlan logicalPlan) {
        if(!Parser.readNextWord(command).toLowerCase().equals("table")){
            Parser.throwException(Parser.exceptionMessage);
        }
        command = Parser.cutWord(command);
        String tableName = Parser.readNextWord(command);
        logicalPlan.createContainer.tableName = tableName;
        command = Parser.cutWord(command);
        String entryStr = command.substring(1, command.lastIndexOf(')')).trim();
        String[] entriesRow = Parser.strToArray(entryStr);
        for(String entry: entriesRow){
            String colName = Parser.readNextWord(entry);
            entry = Parser.cutWord(entry);
            String colType = Parser.cutWord(entry);
            entry = Parser.cutWord(entry);
            logicalPlan.createContainer.typeMap.put(colName, colType);
            if(entry.toLowerCase().equals("primary key")){
                logicalPlan.createContainer.primaryKey = colName;
            }
        }
        command = command.substring(command.lastIndexOf(')') + 1).trim();
        if(!Parser.readNextWord(command).toLowerCase().equals("store")){
            Parser.throwException(Parser.exceptionMessage);
        }
        command = Parser.cutWord(command);
        String storageName = Parser.readNextWord(command);
        logicalPlan.createContainer.storageName = storageName;
    }
}
