package mapping;

import mapping.entity.Content;
import mapping.entity.Location;
import mapping.operator.IO;

import java.util.HashMap;
import java.util.Iterator;

public abstract class OpObject {
    public OpObject(){

    }
    public abstract Iterator<HashMap<String, Object>> select(Location location, IO io);
    public abstract boolean insert(Location location, Content content);
    public abstract boolean delete(Location location);
    public abstract boolean update(Location location, Content content);
    public abstract boolean create(Location location);
    public abstract boolean drop(Location location);
}
