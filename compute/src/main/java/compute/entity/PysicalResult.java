package compute.entity;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class PysicalResult {
    public Iterator<LinkedHashMap<String, Object>> columnIter;
    public boolean isSuccess = true;
    public boolean isFilterPushDown = false;
    public boolean isDimensionPushDown = false;
    public boolean isOrderbyPushDown = false;
}
