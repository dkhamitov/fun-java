package d.kh.fun.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
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

        var cap = 1 << 19;
        var lowLen = 5;
        var highLen = 15;
        var rates = new double[]{0.5, 0.75};
        int iters = 5;

        for (var rate : rates) {
            System.out.println("Testing with " + rate + " keys on average per bucket");
            times(iters, () -> {
                var map = new HashMap<String, Object>(cap);
                times((int) (cap * rate), () -> {
                    var keyLen = lowLen + rnd.nextInt(highLen - lowLen);
                    var key = times(keyLen, () -> rnd.nextInt(letters.size() - 1))
                            .map(letters::get)
                            .collect(joining());
                    map.put(key, null);
                });
                var table = (Object[]) readField(map, "table");
                var distr = Arrays.stream(table).collect(groupingBy(bucket -> {
                    var bucketSize = 0;
                    while (bucket != null) {
                        bucketSize++;
                        bucket = readField(bucket, "next");
                    }
                    return bucketSize;
                }, counting()));
                System.out.println(distr);
            });
        }
    }

    private static Object readField(Object obj, String name) {
        var type = obj.getClass();
        while (type != null) {
            try {
                var fields = type.getDeclaredFields();
                var field = Arrays.stream(fields).filter(f -> f.getName().equals(name)).findFirst();
                if (field.isPresent()) {
                    field.get().setAccessible(true);
                    return field.get().get(obj);
                } else {
                    type = type.getSuperclass();
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException("Field not found");
    }

    private static void times(int n, Runnable r) {
        for (var i = 0; i < n; i++) {
            r.run();
        }
    }

    private static <T> Stream<T> times(int n, Supplier<T> sup) {
        return IntStream.range(0, n).mapToObj(in -> sup.get());
    }
}
