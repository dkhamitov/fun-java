package d.kh.fun.concurrency.robot;

/**
 * Created by khamitovdm on 25/05/2017.
 */
public interface LegTaskFactory {
    Runnable makeLeftStepTask();

    Runnable makeRightStepTask();
}
