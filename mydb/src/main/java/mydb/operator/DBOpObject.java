package mydb.operator;

import mydb.info.DBInfo;
import mapping.OpObject;
import mapping.entity.Content;
import mapping.operator.IO;
import mapping.entity.Location;
import mydb.utils.ConnectUtils;
import mydb.utils.ResultSetUtils;
import mydb.utils.SerdeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DBOpObject extends OpObject {
    public DBOpObject() {
        super.canFilterPushDown = true;
        super.canDimensionPushDown = true;
        super.canOrderbyPushDown = true;
    }

    @Override
    public Iterator<LinkedHashMap<String, Object>> select(Location location, IO io) {
        location.isFilterPushDown = true;
        location.isDimensionPushDown = true;
        location.isOrderbyPushDown = true;
        StringBuffer colNamesBuffer = new StringBuffer();
        for(String key: location.colNames){
            colNamesBuffer.append(SerdeUtils.objToStringKey(key)).append(", ");
        }
        if(location.colNames.size() > 0){
            colNamesBuffer.delete(colNamesBuffer.length() - 2, colNamesBuffer.length());
        }
        StringBuffer commandBuffer = new StringBuffer("select ");
        commandBuffer.append(colNamesBuffer).append(" from ").append(DBInfo.database).append(".").append(location.tableName)
                .append(getWhere(location)).append(getGroupby(location)).append(getOrderby(location));
        final ResultSet rs = ConnectUtils.runCommandQuery(((DBIO)io).conn, ((DBIO)io).pstm, new String(commandBuffer));
        return new Iterator<LinkedHashMap<String, Object>>() {
            ArrayList<LinkedHashMap<String, Object>> dataMapArr = new ArrayList<>();
            public boolean hasNext() {
                boolean hasNext = false;
                try{
                    hasNext = rs.next();
                    if(hasNext) dataMapArr.add(ResultSetUtils.resultSetToMap(rs));
                }catch (SQLException e){
                    e.printStackTrace();
                }
                return hasNext;
            }
            public LinkedHashMap<String, Object> next() {
                if(dataMapArr.size() > 0){
                    return dataMapArr.remove(0);
                }else {
                    try{
                        rs.next();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    return ResultSetUtils.resultSetToMap(rs);
                }
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean insert(Location location, Content content) {
        StringBuffer colNamesBuffer = new StringBuffer();
        StringBuffer colValuesBuffer = new StringBuffer();
        for(String key: content.valueMap.keySet()){
            colNamesBuffer.append(key).append(", ");
        }
        for(String key: content.valueMap.keySet()){
            colValuesBuffer.append(SerdeUtils.objToStringValue(content.valueMap.get(key))).append(", ");
        }
        if(content.valueMap.size() > 0){
            colNamesBuffer.delete(colNamesBuffer.length() - 2, colNamesBuffer.length());
            colValuesBuffer.delete(colValuesBuffer.length() - 2, colValuesBuffer.length());
        }
        StringBuffer commandBuffer = new StringBuffer("insert into ");
        commandBuffer.append(DBInfo.database).append(".").append(location.tableName)
                .append("(").append(colNamesBuffer).append(") ")
                .append("values(").append(colValuesBuffer).append(")");
        ConnectUtils.runCommand(new String(commandBuffer));
        return true;
    }

    @Override
    public boolean delete(Location location) {
        location.isFilterPushDown = true;
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append("delete from ").append(DBInfo.database).append(".").append(location.tableName)
                .append(getWhere(location));
        ConnectUtils.runCommand(new String(commandBuffer));
        return true;
    }

    @Override
    public boolean update(Location location, Content content) {
        location.isFilterPushDown = true;
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append("update ").append(DBInfo.database).append(".").append(location.tableName).append(" set ").append(content.getAlterStr())
                .append(getWhere(location));
        ConnectUtils.runCommand(new String(commandBuffer));
        return true;
    }

    @Override
    public boolean create(Location location) {
        ConnectUtils.runCommand("create table " + DBInfo.database + "." + location.tableName + "(" + location.getCreateStr() + ")");
        return true;
    }

    @Override
    public boolean drop(Location location) {
        return ConnectUtils.runCommand("drop table if exists " + DBInfo.database + "." + location.tableName);
    }

    @Override
    public ArrayList<String> show() {
        Connection conn = ConnectUtils.getConnection();
        PreparedStatement pstm = null;
        String command = "show tables";
        ResultSet rs = ConnectUtils.runCommandQuery(conn, pstm, command);
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            while(rs.next()){
                arrayList.add(rs.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return arrayList;
    }

    @Override
    public boolean truncate(Location location) {
        return ConnectUtils.runCommand("truncate table " + DBInfo.database + "." + location.tableName);
    }

    protected String getWhere(Location location){
        StringBuffer commandBuffer = new StringBuffer();
        if(location.condition != null && location.condition.size() > 0 && location.condition.get(0).size() > 0){
            commandBuffer.append("where ").append(location.getConditionStr());
        }
        return commandBuffer.length() == 0 ? "" : " " + new String(commandBuffer);
    }
    protected String getGroupby(Location location){
        StringBuffer commandBuffer = new StringBuffer();
        if(location.groupColNames != null && location.groupColNames.size() > 0){
            commandBuffer.append("group by ").append(location.getGroupbyStr());
        }
        return commandBuffer.length() == 0 ? "" : " " + new String(commandBuffer);
    }
    protected String getOrderby(Location location){
        StringBuffer commandBuffer = new StringBuffer();
        if(location.orderColNames != null && location.orderColNames.size() > 0){
            commandBuffer.append("order by ").append(location.getOrderbyStr());
        }
        return commandBuffer.length() == 0 ? "" : " " + new String(commandBuffer);
    }
}
