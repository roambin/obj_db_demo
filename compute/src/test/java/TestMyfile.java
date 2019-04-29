import compute.Server;
import compute.entity.Result;
import org.junit.Test;

public class TestMyfile extends MyfileTestBase {

    @Test
    public void select(){
        runCommand("select * from test");
        runCommand("select c1 from test where c2 = 'a' order by c2");
        runCommand("select c1,c2 from test where c1=1 and c1 not between 2 and 23 or c2 = 'a' and c2 is null group by c1, c2 order by c1 asc, c2 desc");
    }
    @Test
    public void orderby(){
        server.runCommand("drop table test2");
        server.runCommand("create table test2(c1 int, c2 char(20)) store myfile");
        server.runCommand("insert into test2 values(1, 'a')");
        server.runCommand("insert into test2 values(2, 'b')");
        server.runCommand("insert into test2 values(3, 'c')");
        server.runCommand("insert into test2 values(3, 'a')");
        server.runCommand("insert into test2 values(3, 'b')");
        runCommand("select * from test2 order by c1 desc, c2");
        server.runCommand("drop table test2");
    }
    @Test
    public void groupby(){
        server.runCommand("drop table test2");
        server.runCommand("create table test2(c1 int, c2 char(20)) store myfile");
        server.runCommand("insert into test2 values(1, 'a')");
        server.runCommand("insert into test2 values(2, 'b')");
        server.runCommand("insert into test2 values(3, 'c')");
        server.runCommand("insert into test2 values(3, 'a')");
        server.runCommand("insert into test2 values(3, 'b')");
        runCommand("select c1, max(c2) from test2 group by c1");
        server.runCommand("drop table test2");
    }
    @Test
    public void statistic(){
        runCommand("select avg(c1), sum(c1), min(c1), max(c2), count(c2) from test");
    }
    @Test
    public void insert(){
        String command = "insert into test(c1, c2) values(1, 'a')";
        runCommand(command);
    }
    @Test
    public void update(){
        String command = "update test set c1 = 2, c2 = 'b' where c1 = 1 and c2 = 'a'";
        runCommand(command);
    }
    @Test
    public void delete(){
        String command = "delete from test where c1 = 1 and c2 = 'a'";
        runCommand(command);
    }
    @Test
    public void create(){
        runCommand("drop table test");
        String command = "create table test(c1 int primary key, c2 char(20)) store myfile";
        runCommand(command);
    }
    @Test
    public void drop(){
        String command = "drop table test";
        runCommand(command);
    }
    @Test
    public void truncate(){
        String command = "truncate table test";
        runCommand(command);
    }
    @Test
    public void show(){
        String command = "show tables";
        runCommand(command);
    }
    protected static void runCommand(String command){
        Server server = new Server();
        Result result = server.runCommand(command);
        System.out.println(result.getString());
    }
}
