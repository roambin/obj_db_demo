package compute.utils;

import java.util.HashSet;

public class SetUtils {
    public static HashSet<String> ArrayToSet(String[] array){
        HashSet<String> hashSet = new HashSet<>();
        for(String s: array){
            hashSet.add(s);
        }
        return hashSet;
    }
}
