package compute.entity.container;

public class OrderByContainer extends Container {
    public String[] colNames;
    public Boolean[] isAsc;
    public OrderByContainer(String[] colNames, Boolean[] isAsc){
        this.colNames = colNames;
        this.isAsc = isAsc;
    }
}
