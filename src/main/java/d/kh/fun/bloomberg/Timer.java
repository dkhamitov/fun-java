package d.kh.fun.bloomberg;

public interface Timer {
    long[] time(Runnable task, int count);
}
