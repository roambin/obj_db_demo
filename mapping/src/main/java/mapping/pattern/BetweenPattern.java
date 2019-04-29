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
        String betweenStr = isNot ? " not between " : " between ";
        return colName + betweenStr + valueMin + " and " + valueMax;
    }

    @Override
    public boolean isMeet(Object value) {
        if(value == null)   return false;
        boolean isMeet;
        if(value.getClass().equals(String.class)){
            isMeet = value.toString().compareTo(valueMin.toString()) >= 0
                    && value.toString().compareTo(valueMax.toString()) <= 0;
        }else if(value.getClass().equals(Character.class)) {
            isMeet = (Character)value >= (Character)valueMin
                    && (Character)value <= (Character)valueMax;
        }else if(value.getClass().equals(Boolean.class)) {
            isMeet = ((Boolean)value).compareTo((Boolean)valueMin) >= 0
                    &&  ((Boolean)value).compareTo((Boolean)valueMax) <= 0;
        }else if(value.getClass().equals(Integer.class)) {
            isMeet = (Integer)value >= (Integer)valueMin
                    && (Integer)value <= (Integer)valueMax;
        }else if(value.getClass().equals(Float.class)) {
            isMeet = (Float)value >= (Float)valueMin
                    && (Float)value <= (Float)valueMax;
        }else if(value.getClass().equals(Double.class)) {
            isMeet = (Double)value >= (Double)valueMin
                    && (Double)value <= (Double)valueMax;
        }else {
            throw new UnsupportedOperationException("Unsupport type: " + value.getClass());
        }
        return isNot != isMeet;
    }
}
