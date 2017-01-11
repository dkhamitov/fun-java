package d.kh.fun.thread;


import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by khamitovdm on 11/01/2017.
 */
public class InterruptedExceptionTest {
    @Test
    public void interrupt_while_waiting() throws InterruptedException {
        Thread reader = new Thread(() -> {
            try {
                new LinkedBlockingQueue<>().take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        reader.start();
        reader.interrupt();
    }
}
