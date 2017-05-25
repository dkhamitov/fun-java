package d.kh.fun.concurrency.robot;

import java.util.Arrays;
import java.util.List;

/**
 * Created by khamitovdm on 25/05/2017.
 */
public class RobotApp {
    public static void main(String[] args) throws InterruptedException {
        Robot robot = new Robot();

        LegTaskFactory taskFactory = new LegTaskFactoryImpl(robot, Leg.LEFT);

        Runnable leftLegTask = taskFactory.makeLeftStepTask();
        Runnable rightLegTask = taskFactory.makeRightStepTask();

        List<Thread> stepThreads = Arrays.asList(new Thread(leftLegTask), new Thread(rightLegTask));

        stepThreads.forEach(Thread::start);

        Thread.sleep(1_000);

        stepThreads.forEach(Thread::interrupt);

        for (Thread thread : stepThreads) {
            thread.join();
        }

        System.out.println(robot);
    }
}
