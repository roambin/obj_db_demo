package mydb;

import mapping.entity.Content;
import mapping.entity.Location;
import mydb.operator.DBIO;
import mydb.operator.DBOpObject;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class TestMyDB extends DBTestBase{
    public static Location location;
    public static Content content;
    public static DBIO io;
    public static DBOpObject dbOpObject;
    @Test
    public void create(){
        dbOpObject.drop(location);
        assert dbOpObject.create(location);
    }
    @Test
    public void drop(){
        assert dbOpObject.drop(location);
    }
    @Test
    public void select(){
        dbOpObject.insert(location, content);
        HashMap<String, Object> selMap = dbOpObject.select(location, io).next();
        HashMap<String, Object> correctMap = new HashMap<>();
        correctMap.put("a", 1);
        correctMap.put("b", "a");
        assert compareMap(selMap, correctMap);
    }
    @Test
    public void insert(){
        dbOpObject.insert(location, content);
        HashMap<String, Object> selMap = dbOpObject.select(location, io).next();
        HashMap<String, Object> correctMap = new HashMap<>();
        correctMap.put("a", 1);
        correctMap.put("b", "a");
        assert compareMap(selMap, correctMap);
    }
    @Test
    public void update(){
        dbOpObject.insert(location, content);
        content.valueMap.put("a", 2);
        content.valueMap.put("b", "b");
        dbOpObject.update(location, content);
        HashMap<String, Object> selMap = dbOpObject.select(location, io).next();
        HashMap<String, Object> correctMap = new HashMap<>();
        correctMap.put("a", 2);
        correctMap.put("b", "b");
        assert compareMap(selMap, correctMap);
    }
    @Test
    public void delete(){
        dbOpObject.insert(location, content);
        dbOpObject.delete(location);
        boolean hasNext = dbOpObject.select(location, io).hasNext();
        assert !hasNext;
    }
    @Test
    public void show(){
        String[] tableNames = dbOpObject.show().toArray(new String[0]);
        assert Arrays.equals(tableNames, new String[]{"test"});
    }
    private static boolean compareMap(HashMap<String, Object> map1, HashMap<String, Object> map2){
        if(map1.size() != map2.size())  return false;
        for(String key: map1.keySet()){
            if(!map1.get(key).equals(map2.get(key))) return false;
        }
        return true;
    }
}
