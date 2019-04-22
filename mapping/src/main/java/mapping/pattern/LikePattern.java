package mapping.pattern;

public class LikePattern extends Pattern{
    String value;
    public boolean isNot;
    public LikePattern(String colName, String value, boolean isNot) {
        super(colName);
        this.value = value;
        this.isNot = isNot;
    }

    @Override
    public String toString() {
        return colName + " like '" + value + "'";
    }
}
