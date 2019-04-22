package compute.parser;

import compute.entity.LogicalPlan;

public class ShowParser {
    protected static void parseShow(String command, LogicalPlan logicalPlan) {
        logicalPlan.showContainer.content = Parser.readNextWord(command);
    }
}
