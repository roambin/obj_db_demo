package mapping.pattern;

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
        return colName + " " + sign + " " + value;
    }
}
