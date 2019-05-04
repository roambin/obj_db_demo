package mapping.pattern;

import java.util.regex.Matcher;

public class LikePattern extends Pattern{
    String value;
    public boolean isNot;
    public LikePattern(String colName, String value, boolean isNot) {
        super(colName);
        this.value = value;
        this.isNot = isNot;
    }

    @Override
    public String toString() {
        String likeStr = isNot ? " not like " : " like ";
        return colName + likeStr + "'" + value + "'";
    }

    @Override
    public boolean isMeet(Object value) {
        String strPattern = this.value;
        strPattern = strPattern.replace("_", "([\\s\\S])");
        strPattern = strPattern.replace("%", "([\\s\\S]*)");
        java.util.regex.Pattern regexPattern = java.util.regex.Pattern.compile(strPattern);
        Matcher matcher = regexPattern.matcher(value.toString());
        if(matcher.find())  return matcher.group(0).length() == value.toString().length();
        else    return false;
    }
}