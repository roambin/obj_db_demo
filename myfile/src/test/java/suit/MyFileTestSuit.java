package suit;

import mydb.TestMyFileDB;
import myfile.TestMyFile;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestMyFileDB.class, TestMyFile.class})
public class MyFileTestSuit {

}
