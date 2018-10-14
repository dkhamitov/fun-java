package d.kh.fun.bloomberg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given N people and the prices for their travel to London and San Francisco, calculate
 * an optimal cost of sending half people to London and the other half to San Francisco. E.g.
 * |Person|London|San Fra|
 * |P1    |100   |50     |
 * |P2    |200   |30     |
 * |P3    |370   |130    |
 * |P4    |40    |170    |
 */
public class StreamingCostOptimizer {
    /*
    N people. Each person can end up in either of 2 cities. So it's 2^N possible solutions of who should
    fly where. But it doesn't take into account the requirement of splitting the group into halves. We
    shouldn't consider the solutions which don't split the groups into halves.
    There are only N/2 slots at the beginning for each city.

    Here is number representation of the problem, where 0 - London, 1 - San Francisco

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

    1100 is the normalized form of all the above solutions

    1a 1b 0  0
    1a 0  1b 0
    1a 0  0  1b

    1b 1a 0  0  -
    0  1a 1b 0
    0  1a 0  1b

    1b 0  1a 0  -
    0  1b 1a 0  -
    0  0  1a 1b

    1b 0  0  1a -
    0  1b 0  1a -
    0  0  1b 1a -




    L1 L2 SF SF
    L1 SF L2 SF
    L1 SF SF L2

    L2 L1 SF SF -
    SF L1 L2 SF
    SF L1 SF L2

    L2 SF L1 SF -
    SF L2 L1 SF -
    SF SF L1 L2

    L2 SF SF L1 -
    SF L2 SF L1 -
    SF SF L2 L1 -

    a b c d e
      b c d e

    a b
    a c
    a d
    a e

    N * (N - 1) = N! / (N - 2)!       - permutations, 2 out of N (or something - to check this in Cormen)
    N * (N - (K - 1)) = N! / (N - K)! - permutations, general form of the above. It would be K out of N
    N! / ((N - K)! * K!)              - combinations

    N * (N - 1) * (N - 2) * (N - (N/2 + 1)) = N! / (N - N/2)! - permutations
    N! / ((N - N/2)! * (N/2)!) = N! / ((N/2)! * (N/2)!)       - combinations

    If N = 4 then
    4! / (2! * 2!) = 24 / 4 = 6

    N! * N! = (1 * 2 * 3 * ... * (N - 1) * N) * (1 * 2 * 3 * ... * (N - 1) * N)
    (N^2)! > N! * N!

    1307674368000
    1.710012252724199e24
    155117520.000000027289901
    155 117 520
    155117520
     */
    DatasetGenerator datasetGenerator;
    Timer timer;

    public StreamingCostOptimizer(DatasetGenerator datasetGenerator, Timer timer) {
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
        var ps = datasetGenerator.generate(30, 2);
        var count = 20;
        var timings = timer.time(() -> combinations(ps), count);
        System.out.println(Arrays.toString(timings));


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
    int[][] combinations(int[][] ps) {
        var n = ps.length;
        //the path shows how the tickets are distributed among the given people
        var pathBuf = new int[n];
        //maintains the path's summary so far, e.g. we need to stop calculation once we detect
        //that we miss a requirement. It can be a requirement of equal numbers of different values in the path.
        //For instance, the path 1010 contains two 1s and two 0s.
        var pathBufSumm = new int[ps[0].length];
        //num - is a number of possible valid tickets' distribution among the people
//        var num = fact(N) / (fact(N / 2) * fact(N / 2));
        //res - is a list of possible valid ticket's distribution among the people
        var res = new ArrayList<int[]>();
        run(ps, 0, pathBuf, pathBufSumm, res);
        return res.toArray(new int[][]{{}});
    }

    void run(int[][] ps, int k, int[] path, int[] pathSumm, List<int[]> res) {
        if (failFast(ps, pathSumm)) {
            return;
        }
        if (k == path.length) {
            res.add(Arrays.copyOf(path, path.length));
        } else {
            var prices = ps[k];
            for (var i = 0; i < prices.length; i++) {
                path[k] = i;
                pathSumm[i]++;
                run(ps, k + 1, path, pathSumm, res);
                pathSumm[i]--;
            }
        }
    }

    boolean failFast(int[][] ps, int[] pathSumm) {
        for (var i = 0; i < pathSumm.length; i++) {
            if (pathSumm[i] > ps.length / 2) {
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
        if (l >= r) {
            return;
        } else {
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

//    private static int fact(int n) {
//        var res = 1;
//        for (int i = 2; i <= n; i++) {
//            res *= i;
//        }
//        return res;
//    }

    void print(List<int[]> res, int[] costs) {
        for (var i = 0; i < res.size(); i++) {
            System.out.println(Arrays.toString(res.get(i)) + " " + costs[i]);
        }
    }
}
