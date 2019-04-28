package compute.parser;

import compute.entity.LogicalPlan;

import java.util.ArrayList;

public class Parser {
    public static String replaceString = "#";
    public static String exceptionMessage = "illegal command";
    public LogicalPlan parse(String command){
        LogicalPlan logicalPlan = new LogicalPlan();
        command = command.trim();
        int firstIndex = command.indexOf(' ');
        if(firstIndex == -1)   throwException(exceptionMessage);
        String firstWord = command.substring(0, firstIndex);
        command = command.substring(firstIndex).trim();
        switch (firstWord.toLowerCase()){
            case "select":
                SelectParser.parse(command, logicalPlan);
                logicalPlan.type = "select";
                break;
            case "insert":
                InsertParser.parse(command, logicalPlan);
                logicalPlan.type = "insert";
                break;
            case "update":
                UpdateParser.parse(command, logicalPlan);
                logicalPlan.type = "update";
                break;
            case "delete":
                DeleteParser.parse(command, logicalPlan);
                logicalPlan.type = "delete";
                break;
            case "create":
                CreateParser.parse(command, logicalPlan);
                logicalPlan.type = "create";
                break;
            case "drop":
                DropParser.parse(command, logicalPlan);
                logicalPlan.type = "drop";
                break;
            case "truncate":
                TruncateParser.parse(command, logicalPlan);
                logicalPlan.type = "truncate";
                break;
            case "show":
                ShowParser.parse(command, logicalPlan);
                logicalPlan.type = "show";
                break;
        }
        return logicalPlan;
    }

    public static String formatString(String command, ArrayList strList){
        StringBuffer buffer = new StringBuffer();
        char[] commandWords = command.toCharArray();
        boolean isString = false;
        boolean isTrans = false;
        StringBuffer word = new StringBuffer();
        for(char c: commandWords){
            switch (c){
                case '\\':
                    if(isString && !isTrans){
                        isTrans = true;
                    } else if(isString && isTrans){
                        isTrans = false;
                        word.append(c);
                    } else throwException(exceptionMessage);
                    break;
                case '\'':
                    if(!isString && !isTrans){
                        isString = true;
                    } else if(isString && isTrans){
                        isTrans = false;
                        word.append(c);
                    } else if(isString && !isTrans){
                        isString = false;
                        strList.add(new String(word));
                        word.setLength(0);
                        buffer.append(replaceString);
                    } else throwException(exceptionMessage);
                    break;
                default:
                    if(isString){
                        word.append(c);
                    }else {
                        buffer.append(c);
                    }
            }
        }
        return new String(buffer);
    }
    public static String readNextWord(String command){
        int indexSplit = getWordSplitIndex(command);
        if(indexSplit == -1){
            return command.trim();
        }
        return command.trim().substring(0, indexSplit);
    }
    public static String cutWord(String command){
        int indexSplit = getWordSplitIndex(command);
        if(indexSplit == -1){
            return "";
        }
        return command.substring(indexSplit).trim();
    }
    private static int getWordSplitIndex(String command){
        int indexBlanek =  command.indexOf(' ');
        int indexBracket = command.indexOf('(');
        if(indexBracket == 0){
            int bracketRightIndex = command.indexOf(')');
            if(bracketRightIndex != -1) indexBracket = bracketRightIndex + 1;
        }
        int indexSplit = Math.min(indexBlanek, indexBracket);
        if(indexSplit == -1)    indexSplit = Math.max(indexBlanek, indexBracket);
        return indexSplit;
    }
    public static String[] strToArray(String command){
        String[] colNamesRow = command.split(",", -1);
        String[] colNames = new String[colNamesRow.length];
        for(int i = 0; i < colNames.length; i++){
            colNames[i] = colNamesRow[i].trim();
        }
        return colNames;
    }

    public static void throwException(String content){
        try{
            throw new Exception(content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

