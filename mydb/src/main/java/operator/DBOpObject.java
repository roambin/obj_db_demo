package operator;

import info.DBInfo;
import mapping.OpObject;
import mapping.entity.Content;
import mapping.operator.IO;
import mapping.entity.Location;
import utils.ConnectUtils;
import utils.ResultSetUtils;
import utils.SerdeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

public class DBOpObject extends OpObject {
    public DBOpObject() {

    }

    @Override
    public Iterator<HashMap<String, Object>> select(Location location, IO io) {
        StringBuffer colNamesBuffer = new StringBuffer();
        for(String key: location.colNames){
            colNamesBuffer.append(SerdeUtils.objToStringKey(key)).append(", ");
        }
        if(location.colNames.size() > 0){
            colNamesBuffer.delete(colNamesBuffer.length() - 2, colNamesBuffer.length());
        }
        StringBuffer commandBuffer = new StringBuffer("select ");
        commandBuffer.append(colNamesBuffer).append(" from ").append(DBInfo.database).append(".").append(location.tableName);
        final ResultSet rs = ConnectUtils.runCommandQuery(((DBIO)io).conn, ((DBIO)io).pstm, new String(commandBuffer));
        return new Iterator<HashMap<String, Object>>() {
            public boolean hasNext() {
                boolean hasNext = false;
                try{
                    hasNext = rs.isLast();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                return hasNext;
            }
            public HashMap<String, Object> next() {
                try{
                    rs.next();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                return ResultSetUtils.resultSetToMap(rs);
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
        for(String key: content.keyValue.keySet()){
            colNamesBuffer.append(key).append(", ");
        }
        for(String key: content.keyValue.keySet()){
            colValuesBuffer.append(SerdeUtils.objToStringValue(content.keyValue.get(key))).append(", ");
        }
        if(content.keyValue.size() > 0){
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
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append("delete from ").append(location.tableName)
                .append(" ").append(getWhereClause(location));
        ConnectUtils.runCommand(new String(commandBuffer));
        return true;
    }

    @Override
    public boolean update(Location location, Content content) {
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append("update ").append(location.tableName).append(" set ").append(content.getAlterStr())
                .append(" ").append(getWhereClause(location));
        ConnectUtils.runCommand(new String(commandBuffer));
        return true;
    }

    @Override
    public boolean create(Location location) {
        ConnectUtils.runCommand("create table " + location.tableName + "(" + location.getCreateStr() + ")");
        return true;
    }

    @Override
    public boolean drop(Location location) {
        ConnectUtils.runCommand("drop table if exists " + location.tableName);
        return true;
    }

    protected String getWhereClause(Location location){
        StringBuffer commandBuffer = new StringBuffer();
        if(location.condition.size() > 0){
            commandBuffer.append("where ").append(location.getConditionStr());
        }
        return new String(commandBuffer);
    }
}
