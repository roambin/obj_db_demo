import compute.parser.Parser;
import org.junit.Test;

public class TestParser extends MyfileTestBase {

    @Test
    public void select(){
        String command = "select max(c1),c2 from test where c1=1 and c1 not between 2 and 23 or c2 = 'a' and c2 is null group by c1, c2 order by c1 asc, c2 desc";
        parseCommand(command);
    }
    @Test
    public void where(){
        String command = "select c1,c2 from test where c2 like '_%' and (c1 not between 2 and 23 or c2 in ('a', 'b') and c2 is null or (c1 = 1 or c1 != 2)) and c1 in (1, 2) or c1=1";
        parseCommand(command);
    }
    @Test
    public void insert(){
        String command = "insert into test(c1, c2) values(1, 'a')";
        parseCommand(command);
    }
    @Test
    public void update(){
        String command = "update test set c1 = 2, c2 = 'b' where c1 = 1 and c2 = 'a'";
        parseCommand(command);
    }
    @Test
    public void delete(){
        String command = "delete from test where c1 = 1 and c2 = 'a'";
        parseCommand(command);
    }
    @Test
    public void create(){
        String command = "create table test(c1 int primary key, c2 char(20)) store mydb";
        parseCommand(command);
    }
    @Test
    public void drop(){
        String command = "drop table test";
        parseCommand(command);
    }
    @Test
    public void truncate(){
        String command = "truncate table test";
        parseCommand(command);
    }
    @Test
    public void show(){
        String command = "show tables";
        parseCommand(command);
    }
    protected static void parseCommand(String command){
        Parser parser = new Parser();
        System.out.println(command);
        parser.parse(command);
    }
}
