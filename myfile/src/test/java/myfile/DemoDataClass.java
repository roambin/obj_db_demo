package myfile;

import java.io.Serializable;
import java.util.Arrays;

public class DemoDataClass implements Serializable {
    public String cstring;
    public Float cfloat;
    public int[] cints;
    public DemoDataClass(String cstring, Float cfloat, int... cints){
        this.cstring = cstring;
        this.cfloat = cfloat;
        this.cints = cints;
    }
    @Override
    public boolean equals(Object obj){
        DemoDataClass demoDataObj = (DemoDataClass)obj;
        boolean isEqual = this.cstring.equals(demoDataObj.cstring)
                && this.cfloat.equals(demoDataObj.cfloat)
                && Arrays.equals(this.cints, demoDataObj.cints);
        return isEqual;
    }
}
