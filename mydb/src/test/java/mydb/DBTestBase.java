package mydb;

import mapping.entity.Content;
import mapping.entity.Location;
import mapping.pattern.ComparePattern;
import mapping.pattern.Pattern;
import mydb.operator.DBIO;
import mydb.operator.DBOpObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ArrayList;

public class DBTestBase {
    @BeforeClass
    public static void beforeClass(){
        TestMyDB.io = new DBIO();
        TestMyDB.location = new Location("test");
        TestMyDB.content = new Content();
        TestMyDB.dbOpObject = new DBOpObject();
        TestMyDB.location.colNames.add("a");
        TestMyDB.location.colNames.add("b");
        TestMyDB.location.colTypes.add("int");
        TestMyDB.location.colTypes.add("varchar(20)");
        TestMyDB.content.valueMap.put("a", 1);
        TestMyDB.content.valueMap.put("b", "a");
        ArrayList<Pattern> andList = new ArrayList<>();
        andList.add(new ComparePattern("a", 5, "<="));
        TestMyDB.location.condition.add(andList);
        TestMyDB.io.open();
    }
    @AfterClass
    public static void afterClass(){
        TestMyDB.io.close();
    }
    @Before
    public void before(){
        TestMyDB.dbOpObject.drop(TestMyDB.location);
        TestMyDB.dbOpObject.create(TestMyDB.location);
    }
    @After
    public void after(){
        TestMyDB.dbOpObject.drop(TestMyDB.location);
    }

}
