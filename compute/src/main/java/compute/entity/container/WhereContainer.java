package compute.entity.container;

import mapping.pattern.Pattern;

import java.util.*;

public class WhereContainer {
    WhereContainer parent;
    ArrayList<WhereContainer> children;
    public Pattern pattern;
    public ArrayList<ArrayList<Pattern>> condition = null;
    public HashSet<String> colNameSet = new HashSet<>();
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
    public ArrayList<ArrayList<Pattern>> generateCondition(boolean isFreshen){
        if(condition != null && !isFreshen) return condition;
        colNameSet.clear();
        condition = new ArrayList<>();
        ArrayList<WhereContainer> leaves = new ArrayList<>();
        preScan(this, leaves);
        for(WhereContainer node: leaves){
            ArrayList<Pattern> patterns = new ArrayList<>();
            while(node.parent != null){
                patterns.add(node.pattern);
                colNameSet.add(node.pattern.colName);
                node = node.parent;

            }
            if(patterns.size() > 0){
                condition.add(patterns);
            }
        }
        return condition;
    }
    public void preScan(WhereContainer whereContainer, ArrayList<WhereContainer> leaves) {
        for(WhereContainer child: whereContainer.children){
            if(child.children.size() == 0)   leaves.add(child);
            else preScan(child, leaves);
        }
    }
}
