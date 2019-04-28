package mapping;

import mapping.entity.Content;
import mapping.entity.Location;
import mapping.operator.IO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class OpObject {
    public boolean canFilterPushDown = false;
    public boolean canDimensionPushDown = false;
    public boolean canOrderbyPushDown = false;
    public OpObject(){

    }
    public abstract Iterator<LinkedHashMap<String, Object>> select(Location location, IO io);
    public abstract boolean insert(Location location, Content content);
    public abstract boolean delete(Location location);
    public abstract boolean update(Location location, Content content);
    public abstract boolean create(Location location);
    public abstract boolean drop(Location location);
    public abstract ArrayList<String> show();
    public boolean truncate(Location location){
        return drop(location) && create(location);
    }
}
