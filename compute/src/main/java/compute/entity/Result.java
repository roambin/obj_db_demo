package compute.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Result {
    public String[] colNames;
    public ArrayList<LinkedHashMap<String, Object>> dataMapArr = null;
    public boolean isSuccess = true;
    public String message;
    public String command;
    public Result(boolean isSuccess){
        this.isSuccess = isSuccess;
    }
    public Result(String message, boolean isSuccess){
        this.message = message;
        this.isSuccess = isSuccess;
    }
    public Result(Iterator<LinkedHashMap<String, Object>> iterator, String[] colNames){
        this.colNames = colNames;
        dataMapArr = new ArrayList<>();
        while(iterator.hasNext()){
            dataMapArr.add(iterator.next());
        }
    }
    public Result(ArrayList<LinkedHashMap<String, Object>> dataMapArr, String[] colNames){
        this.colNames = colNames;
        this.dataMapArr = dataMapArr;
    }
    public String getString() {
        String resultStr = "";//command + "\n";
        if(dataMapArr == null){
            if(message != null)   resultStr += message;
            resultStr += isSuccess;
            return resultStr;
        }
        resultStr += "\n" + dataMapArrToString();
        return resultStr + "\n";
    }
    public String dataMapArrToString(){
        String splitStr = "\t\t";
        StringBuffer stringBuffer = new StringBuffer();
        for(String colName: colNames){
            stringBuffer.append(colName).append(splitStr);
        }
        if(stringBuffer.length() != 0){
            stringBuffer.delete(stringBuffer.length() - splitStr.length(), stringBuffer.length());
        }
        stringBuffer.append('\n');
        for(LinkedHashMap<String, Object> map: dataMapArr){
            stringBuffer.append('\n');
            for(String colName: colNames){
                stringBuffer.append(map.get(colName)).append(splitStr);
            }
            if(dataMapArr.size() == 0){
                stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
            }else {
                stringBuffer.delete(stringBuffer.length() - splitStr.length(), stringBuffer.length());
            }
        }
        return new String(stringBuffer);
    }
}
