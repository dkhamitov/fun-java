package d.kh.fun.lambda;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * Created by khamitovdm on 19/07/2017.
 */
class AmbiguousMath {
    public static void main(String[] args) {
        AB x1 = new AB(1, 2);
        AB x2 = new AB(3, 4);
        List<AB> xs = Arrays.asList(x1, x2);
        int sum = sum(xs, x -> x.getA() + x.getB());
        System.out.println(sum);
    }

    static <T> int sum(Collection<T> ts, ToIntFunction<T> mapper) {
        return ts.stream().mapToInt(mapper).sum();
    }

    /**
     * If we uncomment this method then we get "d/kh/fun/lambda/AmbiguousMath.java:17: error:
     * reference to sum is ambiguous" according to JLS8 15.12.2.2
     */
//    static <T> int sum(Collection<T> ts, Function<T, Integer> mapper) {
//        return ts.stream().mapToInt(mapper::apply).sum();
//    }
}

class AB {
    private final int a;
    private final int b;

    AB(int a, int b) {
        this.a = a;
        this.b = b;
    }

    int getA() {
        return a;
    }

    int getB() {
        return b;
    }
}
