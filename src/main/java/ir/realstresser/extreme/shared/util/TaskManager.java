package ir.realstresser.extreme.shared.util;

import ir.realstresser.extreme.velocity.Main;
import lombok.Cleanup;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("unused")
public class TaskManager {
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final Queue<Runnable> repeatingTaskQueue = new LinkedList<>();
    private boolean isProcessing = false;
    public TaskManager() {}

    public synchronized void addTask(final Runnable task) {
        taskQueue.add(task);
        if (!isProcessing) processQueue();
    }

    private synchronized void processQueue() {
        if (taskQueue.isEmpty())
            return;


        isProcessing = true;

        final Runnable task = taskQueue.poll();

        new Thread(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Main.getInstance().getLogger().error(e.getMessage());
            } finally {
                processQueue();
            }
        }).start();

        isProcessing = false;
    }
    public synchronized void repeatingTask(final Runnable runnable, final long interval) {
        new Thread(() -> {
            while (true) {
                try {
                    runnable.run();
                    Thread.sleep(interval);
                } catch (Exception e) {
                    Main.getInstance().getLogger().error(e.getMessage());
                }
            }
        }).start();
    }
}
