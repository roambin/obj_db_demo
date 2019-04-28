package myfile.operator;

import mapping.OpObject;
import mapping.entity.Content;
import mapping.entity.VirtualColumn;
import mapping.operator.IO;
import mapping.entity.Location;
import myfile.utils.JavassistUtils;
import myfile.utils.TransformUtils;

import java.util.*;

public class FileOpObject extends OpObject {
    public FileOpObject() {

    }

    @Override
    public Iterator<LinkedHashMap<String, Object>> select(Location location, IO io) {
        FileIterator objIter = FileOperator.select(location.tableName);
        return new Iterator<LinkedHashMap<String, Object>>() {
            public boolean hasNext() {
                return objIter.hasNext();
            }
            public LinkedHashMap<String, Object> next() {
                LinkedHashMap<String, Object> map = TransformUtils.getContent(objIter.next()).valueMap;
                map.put(VirtualColumn.colIndex, objIter.getName());
                return map;
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
        boolean isDropClass = JavassistUtils.dropGenerateClass(location.tableName);
        boolean isDropData = FileOperator.truncate(location.tableName);
        return isDropClass && isDropData;
    }

    @Override
    public boolean truncate(Location location) {
        return FileOperator.truncate(location.tableName);
    }

    @Override
    public ArrayList<String> show() {
        throw new UnsupportedOperationException("myfile not support 'show tables', you can find classes directly");
        //return new ArrayList<>(Arrays.asList(FileOperator.listClassNames()));
    }
}
