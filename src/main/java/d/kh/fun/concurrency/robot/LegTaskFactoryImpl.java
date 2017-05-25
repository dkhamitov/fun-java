package d.kh.fun.concurrency.robot;

/**
 * Created by khamitovdm on 25/05/2017.
 */
public class LegTaskFactoryImpl implements LegTaskFactory {
    private final Robot robot;
    private final Leg startLeg;

    public LegTaskFactoryImpl(Robot robot, Leg startLeg) {
        this.robot = robot;
        this.startLeg = startLeg;
    }

    @Override
    public Runnable makeLeftStepTask() {
        return withStartStepTask(interruptableTask(() -> {
            while (robot.leftStepCount() > robot.rightStepCount()) {
                robot.wait();
            }
            robot.leftStep();
        }));
    }

    @Override
    public Runnable makeRightStepTask() {
        return withStartStepTask(interruptableTask(() -> {
            while (robot.rightStepCount() > robot.leftStepCount()) {
                robot.wait();
            }
            robot.rightStep();
        }));
    }

    private Runnable withStartStepTask(Runnable task) {
        return () -> {
            synchronized (robot) {
                if (robot.leftStepCount() == robot.rightStepCount()) {
                    switch (startLeg) {
                        case LEFT:
                            robot.leftStep();
                            break;
                        case RIGHT:
                            robot.rightStep();
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown leg");
                    }
                }
            }
            task.run();
        };
    }

    private Runnable interruptableTask(InterruptableRobotTask task) {
        return () -> {
            Thread thread = Thread.currentThread();
            while (!thread.isInterrupted()) {
                try {
                    synchronized (robot) {
                        task.run();
                        robot.notifyAll();
                        robot.wait();
                    }
                } catch (InterruptedException e) {
                    thread.interrupt();
                    System.out.println(thread + " is interrupted");
                }
            }
        };
    }

    private interface InterruptableRobotTask {
        void run() throws InterruptedException;
    }
}
