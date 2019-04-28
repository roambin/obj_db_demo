package mapping.entity;

import java.util.ArrayList;

public class Comparer implements Comparable {
    public Object value;
    public int index;
    public Comparer(Object value){
        this.value = value;
    }
    public Comparer(Object value, int index){
        this.value = value;
        this.index = index;
    }
    @Override
    public int compareTo(Object o) {
        Object value = ((Comparer)o).value;
        if(this.value == null && value == null)   return 0;
        else if(this.value == null) return -1;
        else if(value == null) return 1;
        int comapreRs;
        if(this.value.getClass().equals(String.class)){
            comapreRs = this.value.toString().compareTo(value.toString());
        }else if(this.value.getClass().equals(Character.class)) {
            comapreRs = ((Character)this.value).compareTo((Character)value);
        }else if(this.value.getClass().equals(Boolean.class)) {
            comapreRs = ((Boolean)this.value).compareTo((Boolean)value);
        }else if(this.value.getClass().equals(Integer.class)) {
            comapreRs = ((Integer)this.value).compareTo((Integer)value);
        }else if(this.value.getClass().equals(Float.class)) {
            comapreRs = ((Float)this.value).compareTo((Float)value);
        }else if(this.value.getClass().equals(Double.class)) {
            comapreRs = ((Double)this.value).compareTo((Double)value);
        }else {
            throw new UnsupportedOperationException("Unsupport type: " + value.getClass());
        }
        return comapreRs;
    }
    static public ArrayList<Object> getValueList(ArrayList<Comparer> comparers){
        ArrayList<Object> valueList = new ArrayList<>();
        for(Comparer comparer: comparers){
            valueList.add(comparer.value);
        }
        return valueList;
    }
}
