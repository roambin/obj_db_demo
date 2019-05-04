package mydb;

import mapping.entity.Content;
import mapping.entity.VirtualColumn;
import mapping.operator.IO;
import mapping.entity.Location;
import myfile.operator.FileOpObject;
import org.junit.Test;

import java.util.HashMap;

public class TestMyFileDB extends MyFileDBTestBase {
    public static Location location;
    public static Content content;
    public static IO io;
    public static FileOpObject dbOpObject;
    @Test
    public void create(){
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
        selMap.remove(VirtualColumn.colIndex);
        HashMap<String, Object> correctMap = new HashMap<>();
        correctMap.put("a", 1);
        correctMap.put("b", "a");
        assert compareMap(selMap, correctMap);
        dbOpObject.delete(location);
    }
    @Test
    public void insert(){
        dbOpObject.insert(location, content);
        HashMap<String, Object> selMap = dbOpObject.select(location, io).next();
        selMap.remove(VirtualColumn.colIndex);
        HashMap<String, Object> correctMap = new HashMap<>();
        correctMap.put("a", 1);
        correctMap.put("b", "a");
        assert compareMap(selMap, correctMap);
        dbOpObject.delete(location);
    }
    @Test
    public void update(){
        dbOpObject.insert(location, content);
        content.valueMap.put("a", 2);
        dbOpObject.update(location, content);
        HashMap<String, Object> selMap = dbOpObject.select(location, io).next();
        selMap.remove(VirtualColumn.colIndex);
        HashMap<String, Object> correctMap = new HashMap<>();
        correctMap.put("a", 2);
        correctMap.put("b", "a");
        assert compareMap(selMap, correctMap);
        dbOpObject.delete(location);
    }
    @Test
    public void delete(){
        dbOpObject.insert(location, content);
        dbOpObject.delete(location);
        boolean hasNext = dbOpObject.select(location, io).hasNext();
        assert !hasNext;
    }
    private static boolean compareMap(HashMap<String, Object> map1, HashMap<String, Object> map2){
        if(map1.size() != map2.size())  return false;
        for(String key: map1.keySet()){
            if(!map1.get(key).equals(map2.get(key))) return false;
        }
        return true;
    }
}
