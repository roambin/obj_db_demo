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
//    public static WhereContainer whereCommandToContainer(String command, String tableName, ArrayList<String> strList){
//        WhereContainer whereContainer = new WhereContainer();
//        String[] orCommands = command.split("or", -1);
//        for(String orCommand: orCommands){
//            WhereContainer andContainer = whereContainer;
//            String[] andCommandsRow = orCommand.split("and", -1);
//            for(int i = 0; i < andCommandsRow.length; i++){
//                String condition = andCommandsRow[i];
//                if(condition.contains("between")){
//                    condition += "and" + andCommandsRow[++i];
//                }
//                andContainer = andContainer.addAndPattern(getPattern(condition.trim(), tableName, strList));
//            }
//        }
//        return whereContainer;
//    }
    public static WhereContainer whereCommandToContainer(String command, String tableName, ArrayList<String> strList){
        command = command.trim();
        for(String sign: new String[]{"=", ">", "<", ">=", "<="}){
            if(command.contains(sign)){
                command = command.replace(sign, " " + sign + " ");
                break;
            }
        }
        command = command.replace("! =", " !=");
        WhereContainer whereContainer = innerWhereCommandParser(command, tableName, strList);
        return whereContainer;
    }
    private static WhereContainer innerWhereCommandParser(String command, String tableName, ArrayList<String> strList){
        WhereContainer whereContainer = new WhereContainer();
        WhereContainer seekContainer = null;
        WhereContainer addContainer = null;
        boolean isNextAnd = false;
        while (!command.equals("")){
            boolean isAnd = isNextAnd;
            if(command.startsWith("(")){
                int bracketNum = 1;
                int index = 0;
                outLoop: while (++index < command.length()){
                    switch (command.charAt(index)){
                        case '(':
                            bracketNum++;
                            break;
                        case ')':
                            bracketNum--;
                            if(bracketNum == 0){
                                break outLoop;
                            }
                            break;
                    }
                }
                String innerCommand = command.substring(1, index).trim();
                command = command.substring(index + 1).trim();
                if(command.length() != 0){
                    String andOr = Parser.readNextWord(command).toLowerCase();
                    command = Parser.cutWord(command).trim();
                    isNextAnd = command.equals("and");
                }
                addContainer = innerWhereCommandParser(innerCommand, tableName, strList);
            }else {
                String commandType = Parser.cutWord(command);
                String patternType = Parser.readNextWord(commandType);
                if(patternType.toLowerCase().equals("not")) patternType = Parser.readNextWord(Parser.cutWord(commandType));
                String patternStr = null;
                int andIndex = command.indexOf("and");
                int orIndex = command.indexOf("or");
                if(andIndex == -1 && orIndex == -1){
                    patternStr = command.trim();
                    command = "";
                }else if(andIndex != -1 && andIndex < orIndex || orIndex == -1){
                    isNextAnd = true;
                    patternStr = command.substring(0, andIndex).trim();
                    command = Parser.cutWord(command.substring(andIndex)).trim();
                }else if(orIndex != -1 && orIndex < andIndex || andIndex == -1){
                    isNextAnd = false;
                    patternStr = command.substring(0, orIndex).trim();
                    command = Parser.cutWord(command.substring(orIndex)).trim();
                }
                if(patternType.toLowerCase().equals("between")){
                    patternStr += " and " + Parser.readNextWord(command);
                    command = Parser.cutWord(command).trim();
                    isNextAnd = Parser.readNextWord(command).toLowerCase().equals("and");
                    command = Parser.cutWord(command).trim();
                }
                Pattern pattern = getPattern(patternStr, tableName, strList);
                addContainer = new WhereContainer(pattern);
            }
            if(isAnd){
                seekContainer.addAnd(addContainer);
                seekContainer = addContainer;
            }else {
                whereContainer.addOr(addContainer);
                seekContainer = addContainer;
            }
        }
        return whereContainer;
    }

    public static Pattern getPattern(String command, String tableName, ArrayList<String> strList){
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
