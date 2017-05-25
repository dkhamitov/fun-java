package d.kh.fun.thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by khamitovdm on 11/01/2017.
 */
public class InterruptedExceptionTest {
    @Test
    public void interrupt_while_waiting() throws InterruptedException {
        //given:
        CountDownLatch latch = new CountDownLatch(1);
        Thread hangingThread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                latch.countDown();
            }
        }, "hanging-thread");
        hangingThread.start();

        //when:
        hangingThread.interrupt();

        //then:
        assertTrue("hangingThread should have been interrupted", latch.await(200, MILLISECONDS));
        assertFalse(hangingThread.isInterrupted());
    }
}
