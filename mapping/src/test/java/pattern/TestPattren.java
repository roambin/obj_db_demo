package pattern;

import mapping.pattern.*;
import org.junit.Test;

public class TestPattren {
    @Test
    public void betweenPattern(){
        Pattern pattern = new BetweenPattern("c", 0, 2, true);
        String command = "c between 0 and 2";
        assert(pattern.toString().equals(command));
    }
    @Test
    public void comparePattern(){
        Pattern pattern = new ComparePattern("c", 0, "=");
        String command = "c = 0";
        assert(pattern.toString().equals(command));
    }
    @Test
    public void inPattern(){
        Pattern pattern = new InPattern("c", true);
        String command = "";
        assert(pattern.toString().equals(command));
        pattern = new InPattern("c", true, 1, 2);
        command = "c in ('1', '2')";
        assert(pattern.toString().equals(command));
    }
    @Test
    public void isNullPattern(){
        Pattern pattern = new IsNullPattern("c", true);
        String command = "c is null";
        assert(pattern.toString().equals(command));
    }
    @Test
    public void likePattern(){
        Pattern pattern = new LikePattern("c", "a%", true);
        String command = "c like 'a%'";
        assert(pattern.toString().equals(command));
    }
}
