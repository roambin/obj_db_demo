package compute.entity;

import mapping.entity.Content;
import mapping.entity.Location;

public class PysicalPlan {
    public Location location;
    public Content content;
    public String storageName;
    public PysicalPlan(){
        content = new Content();
    }
}
