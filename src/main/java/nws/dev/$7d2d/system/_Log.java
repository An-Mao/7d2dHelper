package nws.dev.$7d2d.system;

import nws.dev.$7d2d.config.Config;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class _Log {
    // ANSI 转义序列
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private static final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private static final String logFile = "last.log";

    private static final boolean ENABLE_COLOR;

    static {
        // 初始化是否支持 ANSI 颜色
        ENABLE_COLOR = isAnsiSupported();

        // 启动日志写入线程
        Thread logThread = new Thread(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                while (true) {
                    String log = logQueue.take(); // 阻塞获取日志
                    writer.write(log);
                    writer.newLine();
                    writer.flush(); // 实时写入
                }
            } catch (IOException | InterruptedException e) {
                System.err.println(RED + "[Log Error] 日志写入线程出错：" + e.getMessage() + RESET);
            }
        });
        logThread.setDaemon(true); // 守护线程
        logThread.start();
    }

    public static void info(String... msg) {
        log(ENABLE_COLOR ? GREEN : "", "Info", msg);
    }

    public static void error(String... msg) {
        log(ENABLE_COLOR ? RED : "", "Error", msg);
    }

    public static void warn(String... msg) {
        log(ENABLE_COLOR ? YELLOW : "", "Warn", msg);
    }

    public static void debug(String... msg) {
        if (!Config.I.getDatas().isDebug) return;
        log(ENABLE_COLOR ? CYAN : "", "Debug", msg);
    }

    private static void log(String color, String level, String... msg) {
        for (String s : msg) {
            String formattedLog = String.format("%s[%s][%s]%s", color, getTime(), level, s);
            String plainLog = String.format("[%s][%s]%s", getTime(), level, s);

            // 控制台输出
            System.out.println(ENABLE_COLOR ? formattedLog + RESET : plainLog);

            // 文件日志
            logQueue.offer(plainLog); // 不带颜色保存到文件
        }
    }

    private static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    private static boolean isWindowsAnsiSupported() {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) {
            return false; // 非 Windows 系统
        }

        // 获取 Windows 版本
        String version = System.getProperty("os.version");
        String[] parts = version.split("\\.");
        if (parts.length >= 2) {
            try {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                // Windows 10 以下（如 6.1 表示 Windows 7）不支持 ANSI
                return major >= 10 && minor >= 0;
            } catch (NumberFormatException e) {
                return false; // 版本解析失败，假设不支持
            }
        }

        return false;
    }
    private static boolean testAnsiColorSupport() {
        try {
            // 临时尝试输出 ANSI 转义序列 PURPLE "\u001B[31m"
            System.out.print(PURPLE); // 设置红色
            System.out.print("检测是否支持颜色显示");
            System.out.print("\u001B[0m"); // 重置颜色
            System.out.println();
            // 如果没有异常，则认为支持
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isAnsiSupported() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // 判断 Windows 是否支持 ANSI
            return isWindowsAnsiSupported() && testAnsiColorSupport();
        }

        // Unix-like 系统通常支持 ANSI，但可以检查 TERM 环境变量
        String term = System.getenv("TERM");
        return term != null && !term.equals("dumb");
    }

}
