package d.kh.fun.bloomberg;

import java.util.Arrays;

/**
 * Given N people and the prices for their travel to London and San Francisco, calculate an optimal cost of sending
 * half people to London and the other half to San Francisco. E.g. given the following table:
 * |Person|London|San Fra|
 * |P1    |100   |50     |
 * |P2    |200   |30     |
 * |P3    |370   |130    |
 * |P4    |40    |170    |
 * <p>
 * the solution should be:
 * |Person|City   |
 * |P1    |London |
 * |P2    |San Fra|
 * |P3    |San Fra|
 * |P4    |London |
 */
public class StreamingCostOptimizer {
    /*
    N people. Each person can end up in either of 2 cities. So it's 2^N possible solutions of who should fly where.
    But it doesn't take into account the requirement of splitting the group into halves. We shouldn't consider the
    solutions which don't split the groups into halves

    Here is a numeric representation of the problem for a group consisting of 4 people. Where a, b, c, d are people
    and 0 - London, 1 - San Francisco

    abcd

    0000
    0001
    0010
    0011 *

    0100
    0101 *
    0110 *
    0111

    1000
    1001 *
    1010 *
    1011

    1100 *
    1101
    1110
    1111

    As we can see, the selections are essentially so-called combinations of choosing 2 out of N

    N * (N - 1) = N! / (N - 2)!       - permutations, N choose 2
    N * (N - (K - 1)) = N! / (N - K)! - permutations, general form of the above, N choose K
    N! / ((N - K)! * K!)              - combinations, general form

    N * (N - 1) * (N - 2) * (N - (N/2 + 1)) = N! / (N - N/2)! - permutations
    N! / ((N - N/2)! * (N/2)!) = N! / ((N/2)! * (N/2)!)       - combinations

    If N = 4 then
    4! / (2! * 2!) = 24 / 4 = 6

    (N^2)! > N! * N!
     */
    DatasetGenerator datasetGenerator;
    Timer timer;

    StreamingCostOptimizer(DatasetGenerator datasetGenerator, Timer timer) {
        this.datasetGenerator = datasetGenerator;
        this.timer = timer;
    }

    public static void main(String[] args) {
        var datasetGenerator = new DatasetGeneratorImpl();
        var timer = new TimerImpl();
        var optimizer = new StreamingCostOptimizer(datasetGenerator, timer);
        optimizer.go(args);
    }

    void go(String[] args) {
//        var ps = datasetGenerator.generate(10, 2);
        var ps = new int[][]{{100, 50}, {200, 30}, {370, 130}, {40, 170}};
        var distrs = distributions(ps);
        print(distrs, ps);
//        var count = 20;
//        var timings = timer.time(() -> distributions(ps), count);
//        System.out.println(Arrays.toString(timings));

//        var costs = new int[res.size()];
//        for (var i = 0; i < res.size(); i++) {
//            var path = res.get(i);
//            var cost = 0;
//            for (var j = 0; j < path.length; j++) {
//                var dest = path[j];
//                cost += ps[j][dest];
//            }
//            costs[i] = cost;
//        }
//        System.out.println("Suitable distributions:");
//        print(res, costs);
//        var sortedRefs = sort(costs);
//        var sortedRes = new ArrayList<int[]>();
//        var sortedCosts = new int[res.size()];
//        for (var i = 0; i < res.size(); i++) {
//            var ref = sortedRefs[i];
//            sortedRes.add(res.get(ref));
//            sortedCosts[i] = costs[ref];
//        }
//        System.out.println("Sorted distributions (low cost to higher):");
//        print(sortedRes, sortedCosts);
    }

    /**
     * @param ps a table representing each person as a row with a price for each city as a column
     * @return an optimal cost
     */
    int[][] distributions(int[][] ps) {
        var n = ps.length;
        //the path shows how the tickets are distributed among the given people
        var distrBuf = new int[n];
        //maintains the path's summary so far, e.g. we need to stop calculation once we detect
        //that we miss a requirement. It can be a requirement of equal numbers of different values in the path.
        //For instance, the path 1010 contains two 1s and two 0s.
        var distrBufSumm = new int[ps[0].length];
        //num - is a number of possible valid tickets' distribution among the people
        var num = fact(n) / (fact(n / 2) * fact(n / 2));
        //res - is a list of possible valid ticket's distribution among the people
        var topDistrsNum = n < 10 ? n : 10;
        var distrs = new int[topDistrsNum][];
        var distrsCost = new int[topDistrsNum];
        Arrays.fill(distrsCost, Integer.MAX_VALUE);
        run(ps, 0, distrBuf, distrBufSumm, distrs, distrsCost);
        return distrs;
    }

    int fact(int n) {
        var fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    void run(int[][] ps, int k, int[] distr, int[] distrSumm, int[][] distrs, int[] distrsCost) {
        if (failFast(ps, distrSumm)) {
            return;
        }
        if (k == distr.length) {
            tryAddToTopDistrs(distrs, distrsCost, distr, ps);
        } else {
            var prices = ps[k];
            for (var i = 0; i < prices.length; i++) {
                distr[k] = i;
                distrSumm[i]++;
                run(ps, k + 1, distr, distrSumm, distrs, distrsCost);
                distrSumm[i]--;
            }
        }
    }

    void tryAddToTopDistrs(int[][] distrs, int[] distrsCost, int[] distr, int[][] ps) {
        var cost = calculateCost(distr, ps);
        var pointcut = distrsCost.length;
        while (pointcut > 0 && cost < distrsCost[pointcut - 1]) {
            pointcut--;
        }
        if (pointcut < distrsCost.length) {
            shiftArray(distrs, distrs.length, pointcut);
            shiftArray(distrsCost, distrsCost.length, pointcut);
            distrs[pointcut] = Arrays.copyOf(distr, distr.length);
            distrsCost[pointcut] = cost;
        }
    }

    int calculateCost(int[] distr, int[][] ps) {
        var cost = 0;
        for (var i = 0; i < distr.length; i++) {
            var priceIndex = distr[i];
            cost += ps[i][priceIndex];
        }
        return cost;
    }

    void shiftArray(Object array, int length, int shift) {
        System.arraycopy(array, shift, array, shift + 1, length - shift - 1);
    }

    boolean failFast(int[][] ps, int[] distrSumm) {
        for (var ds : distrSumm) {
            if (ds > ps.length / 2) {
                return true;
            }
        }
        return false;
    }

    int[] sort(int[] xs) {
        var refs = new int[xs.length];
        for (var i = 0; i < refs.length; i++) {
            refs[i] = i;
        }
        quickSort(xs, refs, 0, xs.length - 1);
        return refs;
    }

    void quickSort(int[] xs, int[] refs, int l, int r) {
        if (l < r) {
            var pivot = l;
            for (var i = l + 1; i <= r; i++) {
                var ref = refs[i];
                if (xs[ref] < xs[refs[pivot]]) {
                    var mem = refs[pivot];
                    refs[pivot] = refs[ref];
                    pivot++;
                    refs[ref] = refs[pivot];
                    refs[pivot] = mem;
                }
            }
            quickSort(xs, refs, l, pivot - 1);
            quickSort(xs, refs, pivot + 1, r);
        }
    }

    void print(int[][] distrs, int[][] ps) {
        for (var distr : distrs) {
            System.out.println(Arrays.toString(distr) + " " + calculateCost(distr, ps));
        }
    }
}
