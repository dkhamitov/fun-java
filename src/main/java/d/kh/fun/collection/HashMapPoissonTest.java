package d.kh.fun.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

public class HashMapPoissonTest {
    public static void main(String[] args) {
        var A = Character.codePointAt("A", 0);
        var Z = Character.codePointAt("Z", 0);
        var a = Character.codePointAt("a", 0);
        var z = Character.codePointAt("z", 0);
        var AZ = IntStream.rangeClosed(A, Z);
        var az = IntStream.rangeClosed(a, z);

        var letters = IntStream.concat(AZ, az).mapToObj(Character::toString).collect(toCollection(ArrayList::new));
        var rnd = new Random();

        var n = 100_000;
        var map = new HashMap<String, Object>(n);
        for (var i = 0; i < n; i++) {
            var key = new StringBuilder();
            for (var j = 0; j < 8; j++) {
                key.append(letters.get(rnd.nextInt(letters.size())));
            }
            map.put(key.toString(), null);
        }
    }
}
