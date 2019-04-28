package compute.parser;

import compute.entity.LogicalPlan;

public class TruncateParser {
    protected static void parse(String command, LogicalPlan logicalPlan) {
        if(!Parser.readNextWord(command).toLowerCase().equals("table")){
            Parser.throwException(Parser.exceptionMessage);
        }
        command = Parser.cutWord(command);
        String tableName = Parser.readNextWord(command);
        logicalPlan.truncateContainer.tableName = tableName;
        logicalPlan.tableName = tableName;
    }
}
