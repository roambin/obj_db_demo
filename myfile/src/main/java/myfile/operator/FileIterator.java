package myfile.operator;

import myfile.utils.SerdeUtils;
import java.io.File;
import java.util.ArrayList;

public class FileIterator {
    ArrayList<File> files;
    int index = -1;
    public FileIterator(ArrayList<File> files){
        this.files = files;
    }
    public boolean hasNext() {
        if(files == null){
            return false;
        }
        return index + 1 < files.size();
    }
    public Object next() {
        if(files == null || !hasNext()){
            throw  new UnsupportedOperationException("next element not exists");
        }
        index++;
        return SerdeUtils.deserialize(files.get(index));
    }
    public String getName() {
        return files.get(index).getName();
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
