package compute.entity.container;

public class DeleteContainer {
    public String tableName;
    public WhereContainer whereContainer;
    public DeleteContainer(){
        whereContainer = new WhereContainer();
    }
}
