package compute.parser;

import compute.Setting;
import compute.entity.LogicalPlan;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateParser {
    protected static void parse(String command, LogicalPlan logicalPlan) {
        if(!Parser.readNextWord(command).toLowerCase().equals("table")){
            Parser.throwException(Parser.exceptionMessage);
        }
        command = Parser.cutWord(command);
        String tableName = Parser.readNextWord(command);
        logicalPlan.createContainer.tableName = tableName;
        logicalPlan.tableName = tableName;
        command = Parser.cutWord(command);
        String entryStr = command.substring(1, command.lastIndexOf(')')).trim();
        String[] entriesRow = Parser.strToArray(entryStr);
        for(String entry: entriesRow){
            ArrayList<String> typeDesc = new ArrayList<>();
            do{
                typeDesc.add(Parser.readNextWord(entry));
                entry = Parser.cutWord(entry);
            }while(!entry.equals(""));
            String colName = typeDesc.remove(0);
            if(typeDesc.size() >= 3 && typeDesc.get(typeDesc.size() - 2).equals("primary") && typeDesc.get(typeDesc.size() - 1).equals("key")){
                logicalPlan.createContainer.primaryKey = colName;
                typeDesc.remove(typeDesc.size() - 1);
                typeDesc.remove(typeDesc.size() - 1);
            }
            StringBuffer colTypeBuffer = new StringBuffer();
            for(String word: typeDesc){
                colTypeBuffer.append(word);
            }
            String colType = new String(colTypeBuffer);
            logicalPlan.createContainer.typeMap.put(colName, colType);
        }
        command = command.substring(command.lastIndexOf(')') + 1).trim();
        if(!Parser.readNextWord(command).toLowerCase().equals("store")){
            logicalPlan.createContainer.storageName = Setting.DEFAULT_STORAGE;
        }else{
            command = Parser.cutWord(command);
            String storageName = Parser.readNextWord(command);
            logicalPlan.createContainer.storageName = storageName;
        }
    }
}
