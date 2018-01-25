package d.kh.fun.stream;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class MapInverse {
    public static void main(String[] args) {
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("A1", Arrays.asList("B1", "B2"));
        map.put("A2", Arrays.asList("B2", "B3"));

        Map<String, List<String>> inversedMap = map.entrySet().stream()
                .flatMap(e -> {
                    List<String> value = e.getValue();
                    return value.stream().map(v -> new SimpleEntry<>(v, e.getKey()));
                }).collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toList())));

        System.out.println(map);
        System.out.println(inversedMap);
    }
}
