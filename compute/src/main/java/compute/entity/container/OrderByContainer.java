package compute.entity.container;

public class OrderByContainer {
    public String[] colNames;
    public boolean[] isAsc;
    public OrderByContainer(String[] colNames, boolean[] isAsc){
        this.colNames = colNames;
        this.isAsc = isAsc;
    }
}
