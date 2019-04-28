package compute.parser;

import compute.entity.LogicalPlan;

public class DropParser {
    protected static void parse(String command, LogicalPlan logicalPlan) {
        if(!Parser.readNextWord(command).toLowerCase().equals("table")){
            Parser.throwException(Parser.exceptionMessage);
        }
        command = Parser.cutWord(command);
        String tableName = Parser.readNextWord(command);
        logicalPlan.dropContainer.tableName = tableName;
        logicalPlan.tableName = tableName;
    }
}
