import compute.Server;
import org.junit.After;
import org.junit.Before;

public class MydbTestBase {
    Server server;
    @Before
    public void before(){
        server = new Server();
        server.runCommand("drop table test");
        server.runCommand("create table test(c1 int primary key, c2 char(20)) store mydb");
        server.runCommand("insert into test values(1, 'a')");
        server.runCommand("insert into test values(3, 'b')");
        server.runCommand("insert into test values(2, 'c')");
        server.runCommand("insert into test values(4, 'c')");
    }
    @After
    public void after(){
        server.runCommand("drop table test");
    }
}
