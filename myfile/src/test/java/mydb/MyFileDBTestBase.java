package mydb;

import mapping.entity.Content;
import mapping.entity.Location;
import mapping.pattern.ComparePattern;
import mapping.pattern.Pattern;
import myfile.operator.FileOpObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ArrayList;

public class MyFileDBTestBase {
    @BeforeClass
    public static void beforeClass(){
        TestMyFileDB.io = null;
        TestMyFileDB.location = new Location("test");
        TestMyFileDB.content = new Content();
        TestMyFileDB.dbOpObject = new FileOpObject();
        TestMyFileDB.location.colNames.add("a");
        TestMyFileDB.location.colNames.add("b");
        TestMyFileDB.location.colTypes.add("int");
        TestMyFileDB.location.colTypes.add("varchar(20)");
        TestMyFileDB.content.valueMap.put("a", 1);
        TestMyFileDB.content.valueMap.put("b", "a");
        ArrayList<Pattern> andList = new ArrayList<>();
        andList.add(new ComparePattern("a", 5, "<="));
        TestMyFileDB.location.condition.add(andList);
    }
    @AfterClass
    public static void afterClass(){

    }
    @Before
    public void before(){
        TestMyFileDB.dbOpObject.drop(TestMyFileDB.location);
        TestMyFileDB.dbOpObject.create(TestMyFileDB.location);
    }
    @After
    public void after(){
        TestMyFileDB.dbOpObject.drop(TestMyFileDB.location);
    }

}
