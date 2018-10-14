package d.kh.fun.bloomberg;

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
public class BatchingCostOptimizer {
    /*
    dmitry

    y
    ry
    yr

    try
    rty
    ryt
    tyr
    ytr
    yrt

    itry

     */

    DatasetGenerator datasetGenerator;
    Timer timer;

    public BatchingCostOptimizer(DatasetGenerator datasetGenerator, Timer timer) {
        this.datasetGenerator = datasetGenerator;
        this.timer = timer;
    }

    public static void main(String[] args) {
        var datasetGenerator = new DatasetGeneratorImpl();
        var timer = new TimerImpl();
        var optimizer = new BatchingCostOptimizer(datasetGenerator, timer);
        optimizer.go(args);
    }

    void go(String[] args) {
        var ps = datasetGenerator.generate(30, 2);
        var count = 20;
        timer.time(() -> permutateBottomUp(ps, ps.length), count);
        timer.time(() -> permutateTopDown(ps, ps.length), count);
//        for (var permutation : permutations) {
//            System.out.println(Arrays.toString(permutation));
//        }
//        int[] costs = calculateTotalCosts(ps, res);
//
//        var sortedRefs = sort(costs);
//        var sortedRes = new ArrayList<int[]>();
//        var sortedCosts = new int[res.size()];
//        for (var i = 0; i < res.size(); i++) {
//            var ref = sortedRefs[i];
//            sortedRes.add(res.get(ref));
//            sortedCosts[i] = costs[ref];
//        }
    }

    int[][] permutations(int[][] ps) {
//        return permutateTopDown(ps, ps.length);
        return permutateBottomUp(ps, ps.length);
    }

    int[][] permutateTopDown(int[][] ps, int k) {
        if (k == 0) {
            return new int[][]{{}};
        } else {
            var distrs = permutateTopDown(ps, k - 1);
            var dests = ps[k - 1];
            var kdistrs = new int[distrs.length * dests.length][];
            for (var i = 0; i < dests.length; i++) {
                for (var j = 0; j < distrs.length; j++) {
                    var distr = distrs[j];
                    var kdistr = new int[distr.length + 1];
                    kdistr[0] = i;
                    System.arraycopy(distr, 0, kdistr, 1, distr.length);
                    kdistrs[i * distrs.length + j] = kdistr;
                }
            }
            return kdistrs;
        }
    }

    int[][] permutateBottomUp(int[][] ps, int n) {
        var distrs = new int[][]{{}};
        for (var k = 0; k < n; k++) {
            var dests = ps[k];
            var kdistrs = new int[distrs.length * dests.length][];
            for (var i = 0; i < dests.length; i++) {
                for (var j = 0; j < distrs.length; j++) {
                    var distr = distrs[j];
                    var kdistr = new int[distr.length + 1];
                    kdistr[0] = i;
                    System.arraycopy(distr, 0, kdistr, 1, distr.length);
                    kdistrs[i * distrs.length + j] = kdistr;
                }
            }
            System.out.println(k + ": " + totalMemory());
            distrs = kdistrs;
        }
        return distrs;
    }

    private long totalMemory() {
        var mem = Runtime.getRuntime().totalMemory();
        return mem / 1024 / 1024;
    }

    int[] calculateTotalCosts(int[][] ps, List<int[]> distrs) {
        var costs = new int[distrs.size()];
        for (var i = 0; i < distrs.size(); i++) {
            var distr = distrs.get(i);
            var cost = 0;
            for (var j = 0; j < distr.length; j++) {
                var dest = distr[j];
                cost += ps[j][dest];
            }
            costs[i] = cost;
        }
        return costs;
    }
}
