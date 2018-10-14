package d.kh.fun.bloomberg;

import java.util.Random;

public class DatasetGeneratorImpl implements DatasetGenerator {
    @Override
    public int[][] generate(int r, int c) {
        var rnd = new Random();
        var ds = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                ds[i][j] = rnd.nextInt(1000);
            }
        }
//        return new int[][]{{100, 050}, {200, 030}, {370, 130}, {040, 170}};
        return ds;
    }
}
