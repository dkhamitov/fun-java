package d.kh.fun.concurrency.robot;

/**
 * Created by khamitovdm on 25/05/2017.
 */
public class Robot {
    private long left;
    private long right;

    public long rightStep() {
        return ++right;
    }

    public long leftStep() {
        return ++left;
    }

    public long rightStepCount() {
        return right;
    }

    public long leftStepCount() {
        return left;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
