package compute.entity.container;

import mapping.pattern.Pattern;

import java.util.ArrayList;

public class WhereContainer {
    WhereContainer parent;
    ArrayList<WhereContainer> children;
    Pattern pattern;
    public WhereContainer(){
        children = new ArrayList<>();
    }
    public WhereContainer(Pattern pattern, WhereContainer whereContainer) {
        parent = whereContainer;
        children = new ArrayList<>();
        this.pattern = pattern;
    }
    public WhereContainer addAndPattern(Pattern pattern){
        WhereContainer whereContainer = new WhereContainer(pattern, this);
        children.add(whereContainer);
        return whereContainer;
    }
}
