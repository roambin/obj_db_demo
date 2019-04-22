package mapping.entity;

import mapping.pattern.Pattern;

import java.util.ArrayList;

public class Location {
    public String tableName;
    public String key = null;
    public ArrayList<String> colNames;
    public ArrayList<String> colTypes;
    public ArrayList<ArrayList<Pattern>> condition;
    public Location(String tableName){
        this.tableName = tableName;
        condition = new ArrayList<ArrayList<Pattern>>();
        colNames = new ArrayList<String>();
        colTypes = new ArrayList<String>();
    }
    public String getConditionStr(){
        StringBuffer orBuffer = new StringBuffer();
        for(ArrayList<Pattern> patterns: condition){
            for(Pattern pattern: patterns){
                orBuffer.append(pattern.toString()).append(" and ");
            }
            orBuffer.delete(orBuffer.length() - 4, orBuffer.length());
            orBuffer.append(" or ");
        }
        orBuffer.delete(orBuffer.length() - 3, orBuffer.length());
        return new String(orBuffer);
    }
    public String getCreateStr(){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < colNames.size(); i++){
            buffer.append(colNames.get(i)).append(" ").append(colTypes.get(i)).append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        return new String(buffer);
    }
}
