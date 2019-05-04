package compute.entity.container;

public class StatisticContainer extends Container {
    public int index;
    public String colname;
    public String statisticType;
    public StatisticContainer(String colname, String statisticType, int index){
        this.colname = colname;
        this.statisticType = statisticType;
        this.index = index;
    }
}
