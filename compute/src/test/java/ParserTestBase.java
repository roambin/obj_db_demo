import compute.Setting;
import compute.entity.TableInfo;
import compute.parser.Parser;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ParserTestBase {
    @BeforeClass
    public static void beforeClass(){
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, "test");
        tableInfo.colTypes.put("c1", "int");
        tableInfo.colTypes.put("c2", "string");
        tableInfo.storeTableInfo();
    }
    @AfterClass
    public static void afterClass(){
        TableInfo tableInfo = new TableInfo(Setting.DATABASE, "test");
        tableInfo.deleteFactTableInfo();
    }
}
