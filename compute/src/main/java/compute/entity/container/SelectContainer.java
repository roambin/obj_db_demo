package compute.entity.container;

public class SelectContainer {
    public String tableName;
    public String[] colNames;
    public WhereContainer whereContainer;
    public GroupByContainer groupByContainer;
    public OrderByContainer orderByContainer;
    public SelectContainer(){
        whereContainer = new WhereContainer();
    }
}
