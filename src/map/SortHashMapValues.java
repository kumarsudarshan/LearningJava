package map;

import java.util.*;
import java.util.stream.Collectors;

public class SortHashMapValues {

    public static void main(String[] args) {
        Map<String, Integer> hm = new HashMap<>();
        hm.put("kumar", 100);
        hm.put("rahul", 50);
        hm.put("narendra", 60);
        hm.put("ashish", 300);
        hm.put("ravi", 10);
        hm.put("john", 67);
        System.out.println(sortByValue(hm));
    }

    public static Map<String, Integer> sortByValue(Map<String, Integer> hm) {
        List<Map.Entry<String, Integer>> lists = new ArrayList<>(hm.entrySet());
        Collections.sort(lists, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        Map<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : lists) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}