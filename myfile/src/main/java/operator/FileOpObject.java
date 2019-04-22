package operator;

import mapping.OpObject;
import mapping.entity.Content;
import mapping.operator.IO;
import mapping.entity.Location;
import utils.JavassistUtils;
import utils.TransformUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class FileOpObject extends OpObject {
    public FileOpObject() {

    }

    @Override
    public Iterator<HashMap<String, Object>> select(Location location, IO io) {
        Iterator objIter = FileOperator.select(location.tableName);
        return new Iterator<HashMap<String, Object>>() {
            int index = -1;
            public boolean hasNext() {
                return objIter.hasNext();
            }
            public HashMap<String, Object> next() {
                return TransformUtils.getContent(objIter.next()).keyValue;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean insert(Location location, Content content) {
        if(location.key == null){
            location.key = UUID.randomUUID().toString();
        }
        return FileOperator.write(location.key, TransformUtils.toObject(location, content));
    }

    @Override
    public boolean delete(Location location) {
        return FileOperator.delete(location.tableName, location.key);
    }

    @Override
    public boolean update(Location location, Content content) {
        return FileOperator.update(location.key, TransformUtils.toObject(location, content));
    }

    @Override
    public boolean create(Location location) {
        return true;
    }

    @Override
    public boolean drop(Location location) {
        JavassistUtils.dropGenerateClass(location.tableName);
        return true;
    }
}
