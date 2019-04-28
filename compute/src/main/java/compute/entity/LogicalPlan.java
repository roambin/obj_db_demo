package compute.entity;

import compute.entity.container.*;

public class LogicalPlan {
    public String type;
    public String tableName;
    public SelectContainer selectContainer;
    public InsertContainer insertContainer;
    public UpdateContainer updateContainer;
    public DeleteContainer deleteContainer;
    public CreateContainer createContainer;
    public DropContainer dropContainer;
    public TruncateContainer truncateContainer;
    public ShowContainer showContainer;
    public LogicalPlan(){
        selectContainer = new SelectContainer();
        insertContainer = new InsertContainer();
        updateContainer = new UpdateContainer();
        deleteContainer = new DeleteContainer();
        createContainer = new CreateContainer();
        dropContainer = new DropContainer();
        truncateContainer = new TruncateContainer();
        showContainer = new ShowContainer();
    }
}
