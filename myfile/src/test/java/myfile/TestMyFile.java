package myfile;

import myfile.operator.FileOperator;
import org.junit.Test;

public class TestMyFile {
    @Test
    public void fileWrite(){
        DemoDataClass obj = new DemoDataClass("a", 1.1f, 1, 2);
        FileOperator.write("test", obj);
        Object selObj = FileOperator.select("myfile.DemoDataClass").next();
        assert obj.equals(selObj);
        FileOperator.delete("myfile.DemoDataClass", "test");
    }
    @Test
    public void fileSelect(){
        DemoDataClass obj = new DemoDataClass("a", 1.1f, 1, 2);
        FileOperator.write("test", obj);
        Object selObj = FileOperator.select("myfile.DemoDataClass").next();
        assert obj.equals(selObj);
        FileOperator.delete("myfile.DemoDataClass", "test");
    }
    @Test
    public void fileUpdate(){
        DemoDataClass obj = new DemoDataClass("a", 1.1f, 1, 2);
        FileOperator.write("test", obj);
        obj.cstring = "b";
        FileOperator.update("test", obj);
        Object selObj = FileOperator.select("myfile.DemoDataClass").next();
        assert obj.equals(selObj);
        FileOperator.delete("myfile.DemoDataClass", "test");
    }
    @Test
    public void fileDelete(){
        DemoDataClass obj = new DemoDataClass("a", 1.1f, 1, 2);
        FileOperator.write("test", obj);
        FileOperator.delete("myfile.DemoDataClass", "test");
        assert !FileOperator.select("myfile.DemoDataClass").hasNext();
    }

}
