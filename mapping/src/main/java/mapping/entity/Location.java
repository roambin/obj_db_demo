package mapping.entity;

import mapping.pattern.Pattern;

import java.util.ArrayList;

public class Location {
    public String tableName;
    public String key = null;
    public ArrayList<String> colNames;
    public ArrayList<String> colTypes;
    public ArrayList<ArrayList<Pattern>> condition;
    public ArrayList<String> groupColNames;
    public ArrayList<String> orderColNames;
    public ArrayList<Boolean> orderIsAsc;
    public boolean isFilterPushDown = false;
    public boolean isDimensionPushDown = false;
    public boolean isOrderbyPushDown = false;
    public Location(String tableName){
        this.tableName = tableName;
        condition = new ArrayList<>();
        colNames = new ArrayList<>();
        colTypes = new ArrayList<>();
    }
    public String getConditionStr(){
        StringBuffer orBuffer = new StringBuffer();
        for(ArrayList<Pattern> patterns: condition){
            for(Pattern pattern: patterns){
                orBuffer.append(pattern.toString()).append(" and ");
            }
            if(orBuffer.length() > 0){
                orBuffer.delete(orBuffer.length() - 5, orBuffer.length());
            }
            orBuffer.append(" or ");
        }
        orBuffer.delete(orBuffer.length() - 4, orBuffer.length());
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
    public String getGroupbyStr(){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < groupColNames.size(); i++){
            buffer.append(groupColNames.get(i)).append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        return new String(buffer);
    }
    public String getOrderbyStr(){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < orderColNames.size(); i++){
            String sortType = orderIsAsc.get(i) ? "asc" : "desc";
            buffer.append(orderColNames.get(i)).append(" ").append(sortType).append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        return new String(buffer);
    }
    public Location clone(){
        Location location = new Location(tableName);
        location.key = key;
        location.isFilterPushDown = isFilterPushDown;
        location.colNames = (ArrayList<String>)colNames.clone();
        location.colTypes = (ArrayList<String>)colTypes.clone();
        location.condition = (ArrayList<ArrayList<Pattern>>)condition.clone();
        return location;
    }
}
