package mapping.pattern;

public class IsNullPattern extends Pattern{
    public boolean isNot;
    public IsNullPattern(String colName, boolean isNot) {
        super(colName);
        this.isNot = isNot;
    }

    @Override
    public String toString() {
        return colName + (isNot ? " is null" : " is not null");
    }
}
