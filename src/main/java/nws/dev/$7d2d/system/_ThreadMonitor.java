package nws.dev.$7d2d.system;

import java.time.LocalTime;

public class _ThreadMonitor {
    private final Runnable task;
    private Thread thread;
    private final long cooldownMillis;
    private final long s;

    public _ThreadMonitor(Runnable task, long cooldownMillis) {
        this.task = task;
        this.cooldownMillis = cooldownMillis;
        this.s = cooldownMillis / 1000;
    }

    public void start() {
        thread = createThread(task);
        //thread.setDaemon(true);
        thread.start();
    }

    private Thread createThread(Runnable task) {
        Thread newThread = new Thread(task);
        newThread.setUncaughtExceptionHandler((t, e) -> {
            _Log.error("线程 " + t.getName() + " 崩溃，原因：" + e.getMessage());
            try {
                _Log.info("等待 " + LocalTime.ofSecondOfDay(s) + " 后重启...");
                Thread.sleep(cooldownMillis); // 添加冷却时间
            } catch (InterruptedException ex) {
                _Log.warn("冷却期间被中断：" + ex.getMessage());
            }
            restart(); // 重启线程
        });
        return newThread;
    }

    private void restart() {
        _Log.info("正在重启线程...");
        thread = createThread(task);
        thread.start();
    }
}
