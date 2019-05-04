package compute.entity.container;

import mapping.pattern.Pattern;

import java.util.*;

public class WhereContainer extends Container {
    public WhereContainer parent;
    public ArrayList<WhereContainer> children;
    public Pattern pattern;
    public ArrayList<ArrayList<Pattern>> condition = null;
    public HashSet<String> colNameSet = new HashSet<>();
    public WhereContainer(){
        children = new ArrayList<>();
    }
    public WhereContainer(Pattern pattern) {
        this.pattern = pattern;
        children = new ArrayList<>();
    }
//    public WhereContainer(Pattern pattern, WhereContainer whereContainer) {
//        parent = whereContainer;
//        whereContainer.children.add(this);
//        children = new ArrayList<>();
//        this.pattern = pattern;
//    }

//    public WhereContainer addAndPattern(Pattern pattern){
//        WhereContainer whereContainer = new WhereContainer(pattern, this);
//        children.add(whereContainer);
//        return whereContainer;
//    }
    public static WhereContainer copy(WhereContainer whereContainer){
        WhereContainer copyContainer = new WhereContainer();
        copyContainer.children = (ArrayList<WhereContainer>)whereContainer.children.clone();
        for(WhereContainer child: copyContainer.children){
            child.parent = copyContainer;
        }
        copyContainer.pattern = whereContainer.pattern;
        return copyContainer;
    }
    public WhereContainer addAnd(WhereContainer addContainer){
        return addAnd(this, addContainer);
    }
    public static WhereContainer addAnd(WhereContainer whereContainer, WhereContainer addContainer){
        if(whereContainer.children.size() == 0){
            WhereContainer copyContainer = copy(addContainer);
            whereContainer.children.add(copyContainer);
            copyContainer.parent = whereContainer;
        }else{
            for(WhereContainer child: whereContainer.children){
                child.addAnd(child, addContainer);
            }
        }
        return whereContainer;
    }
    public void addOr(WhereContainer addContainer){
        this.children.add(addContainer);
        addContainer.parent = this;
    }

    public ArrayList<ArrayList<Pattern>> generateCondition(boolean isFreshen){
        if(condition != null && !isFreshen) return condition;
        colNameSet.clear();
        condition = new ArrayList<>();
        ArrayList<WhereContainer> leaves = new ArrayList<>();
        preScan(this, leaves);
        for(WhereContainer node: leaves){
            HashMap<String, Pattern> patterns = new HashMap<>();
            while(node.parent != null){
                if(node.pattern != null){
                    patterns.put(node.pattern.toString(), node.pattern);
                    colNameSet.add(node.pattern.colName);
                }
                node = node.parent;
            }
            if(patterns.size() > 0){
                condition.add(new ArrayList<>(patterns.values()));
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
