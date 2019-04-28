package mapping.pattern;

public abstract class Pattern {
    public String colName;
    public Pattern(String colName){
        this.colName = colName;
    }
    abstract public boolean isMeet(Object value);

}
