package mapping.pattern;

public class BetweenPattern extends Pattern{
    public Object valueMin;
    public Object valueMax;
    public boolean isNot;
    public BetweenPattern(String colName, Object valueMin, Object valueMax, boolean isNot){
        super(colName);
        this.colName = colName;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.isNot = isNot;
    }

    @Override
    public String toString() {
        return colName + " between " + valueMin + " and " + valueMax;
    }
}
