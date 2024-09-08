package ir.realstresser.extreme.shared.util;

import ir.realstresser.extreme.velocity.VelocityMain;

import java.util.concurrent.*;

public class TaskManager {
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // Adjust the thread pool size as needed
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public TaskManager() {
        startProcessing();
    }

    private void startProcessing() {
        executorService.submit(() -> {
            while (true) {
                try {
                    Runnable task = taskQueue.take();
                    executorService.submit(() -> {
                        try {
                            task.run();
                        } catch (Exception e) {
                            VelocityMain.getInstance().getLogger().error(e.getMessage());
                        }
                    });
                } catch (final Exception e) {
                    VelocityMain.getInstance().getLogger().error(e.getMessage());
                }
            }
        });
    }

    public void addTask(final Runnable task) {
        taskQueue.add(task);
    }

    public void repeatingTask(final Runnable runnable, final long interval, TimeUnit unit) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                runnable.run();
            } catch (final Exception e) {
                VelocityMain.getInstance().getLogger().error(e.getMessage());
            }
        }, 0, interval, unit);
    }
}
