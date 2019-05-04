package compute.entity.container;

public class DeleteContainer extends Container {
    public String tableName;
    public WhereContainer whereContainer;
    public DeleteContainer(){
        whereContainer = new WhereContainer();
    }
}
