package xyz.goome.eternal.timer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by matrix on 16/3/28.
 */
public class TickTimer {

    private static TickTimer instance = new TickTimer();
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = null;

    private TickTimer() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }

    public static TickTimer getInstance() {
        return instance;
    }

    public void tick() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Task(), 5000, 100, TimeUnit.MICROSECONDS);
    }

    class Task implements Runnable {

        @Override
        public void run() {
            //TODO
        }
    }

}
