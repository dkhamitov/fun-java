package d.kh.fun.bloomberg;

import java.util.concurrent.TimeUnit;

public class TimerImpl implements Timer {
    @Override
    public long[] time(Runnable task, int count) {
        var timings = new long[count];
        for (int i = 0; i < count; i++) {
            var t0 = System.nanoTime();
            task.run();
            var t1 = System.nanoTime();
            timings[i] = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        }
        return timings;
    }
}
