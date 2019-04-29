package compute.parser;

import compute.Setting;
import compute.entity.TableInfo;
import compute.entity.container.WhereContainer;
import mapping.pattern.BetweenPattern;
import mapping.pattern.ComparePattern;
import mapping.pattern.IsNullPattern;
import mapping.pattern.Pattern;
import mapping.utils.SerdeUtils;
import java.util.ArrayList;

public class WhereParser {
    public static WhereContainer whereCommandToContainer(String command, String tableName, ArrayList<String> strList){
        WhereContainer whereContainer = new WhereContainer();
        String[] orCommands = command.split("or", -1);
        for(String orCommand: orCommands){
            String[] andCommandsRow = orCommand.split("and", -1);
            ArrayList<String> andCommandsList = new ArrayList<>();
            for(int i = 0; i < andCommandsRow.length; i++){
                String condition = andCommandsRow[i];
                if(condition.contains("between")){
                    condition += "and" + andCommandsRow[++i];
                }
                andCommandsList.add(condition.trim());
            }
            String[] andCommands = new String[andCommandsList.size()];
            for(int i = 0; i < andCommands.length; i++){
                andCommands[i] = andCommandsList.get(i);
            }
            WhereContainer andContainer = whereContainer;
            for(String andCommand: andCommands){
                andContainer = andContainer.addAndPattern(getPattern(andCommand, tableName, strList));
            }
        }
        return whereContainer;
    }
    /*for where pharser
    public static WhereContainer whereCommandToContainer(String command, String tableName, ArrayList<String> strList){
        WhereContainer whereContainer = new WhereContainer();
        String[] orCommands = command.split("or", -1);
        for(String orCommand: orCommands){
            WhereContainer rootContainer = new WhereContainer();
            String[] andCommands = orCommand.split("and", -1);
            for(String unitCommand: andCommands){
                unitCommand = unitCommand.trim();
                if(unitCommand.startsWith("(")){
                    rootContainer.addAnd(whereCommandToContainer(unitCommand.substring(1, unitCommand.length() - 1), tableName, strList));
                }else {
                    rootContainer.addChildPattern(getPattern(unitCommand.trim(), tableName, strList));
                }
            }
            rootContainer = rootContainer.children.get(0);
            whereContainer.addAnd(rootContainer);
        }

        return whereContainer;
    }*/

    public static Pattern getPattern(String command, String tableName, ArrayList<String> strList){
        boolean isReplaced = false;
        for(String sign: new String[]{"!=", ">", "<", ">=", "<="}){
            if(command.contains(sign)){
                command = command.replace(sign, " " + sign + " ");
                isReplaced = true;
                break;
            }
        }
        if(!isReplaced){
            command = command.replace("=", " = ");
        }
        String colName = Parser.readNextWord(command);
        command = Parser.cutWord(command);
        String patternType = Parser.readNextWord(command);
        command = Parser.cutWord(command);
        if(patternType.equals("is") || patternType.equals("not")){
            patternType += " " + Parser.readNextWord(command);
            command = Parser.cutWord(command);
        }
        if(patternType.equals("is not")){
            patternType += " " + Parser.readNextWord(command);
            command = Parser.cutWord(command);
        }
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, tableName);
        tableInfo.getTableInfo();
        patternType = patternType.toLowerCase();
        boolean isNot = patternType.startsWith("not") || patternType.startsWith("is not");
        switch (patternType){
            case "=":
            case "!=":
            case ">":
            case ">=":
            case "<":
            case "<=":
                String valueStr = Parser.readNextWord(command);
                if(valueStr.equals("#")){
                    valueStr = "'" + strList.remove(0) + "'";
                }
                String valueType = tableInfo.getType(colName);
                Object value = SerdeUtils.strToObj(valueStr, valueType);
                return new ComparePattern(colName, value, patternType);
            case "between":
            case "not between":
                String[] valueStrs = command.split("and");
                valueStrs[0] = valueStrs[0].trim();
                valueStrs[1] = valueStrs[1].trim();
                valueType = tableInfo.getType(colName);
                Object[] values = new Object[2];
                for(int i = 0; i < valueStrs.length; i++){
                    values[i] = SerdeUtils.strToObj(valueStrs[i], valueType);
                }
                return new BetweenPattern(colName, values[0], values[1], isNot);
            case "in":
            case "not in":
                throw new UnsupportedOperationException("unsupport type: " + patternType);
            case "like":
            case "not like":
                throw new UnsupportedOperationException("unsupport type: " + patternType);
            case "is null":
            case "is not null":
                return new IsNullPattern(colName, isNot);
            default:
                throw new UnsupportedOperationException("unsupport type: " + patternType);
        }
    }
}
