package mapping.pattern;

import mapping.entity.Comparer;

public class ComparePattern extends Pattern{
    Object value;
    String sign;
    public ComparePattern(String colName, Object value, String sign) {
        super(colName);
        this.value = value;
        this.sign = sign;
    }

    @Override
    public String toString() {
        if(value.getClass().equals(String.class)){
            value = value.toString().replace("'", "\\'");
        }
        return colName + " " + sign + " " + "'" + value + "'";
    }

    @Override
    public boolean isMeet(Object value) {
        if(value == null)   return false;
        Comparer comparer = new Comparer(value);
        int comapreRs = comparer.compareTo(new Comparer(this.value));
        switch (sign){
            case "=":   return comapreRs == 0;
            case "!=":  return comapreRs != 0;
            case ">":   return comapreRs > 0;
            case ">=":  return comapreRs >= 0;
            case "<":   return comapreRs < 0;
            case "<=":  return comapreRs <= 0;
            default:    throw new UnsupportedOperationException("Unsupport sign: " + sign);
        }
    }
}
