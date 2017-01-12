package d.kh.fun.thread;


import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertNotNull;

/**
 * Created by khamitovdm on 11/01/2017.
 */
public class InterruptedExceptionTest {
    @Test
    public void interrupt_while_waiting() throws InterruptedException {
        //given:
        BlockingQueue<Object> signals = new LinkedBlockingQueue<>();
        Thread hangingThread = new Thread(() -> {
            try {
                new LinkedBlockingQueue<>().take();
            } catch (InterruptedException e) {
                signals.add(new Object());
            }
        }, "hanging-thread");
        hangingThread.start();

        //when:
        hangingThread.interrupt();

        //then:
        assertNotNull("hangingThread should have been interrupted", signals.poll(100, MILLISECONDS));
    }
}
