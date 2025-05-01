package nws.dev.$7d2d;

import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.command.ServerCommand;
import nws.dev.$7d2d.config.ACItemsConfig;
import nws.dev.$7d2d.config.ACItemsData;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.DrawConfig;
import nws.dev.$7d2d.event.Events;
import nws.dev.$7d2d.net.ACNet;
import nws.dev.$7d2d.net.Heartbeat;
import nws.dev.$7d2d.net.QQNet;
import nws.dev.$7d2d.system._Log;

import java.util.Scanner;

public class Main {
    private static final Thread inputThread = runInputThread();
    private static boolean isClosing = false;
    static {
        System.setProperty("java.awt.headless", "true");
        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            _Log.info("========程序即将关闭========");
            isClosing = true;
            _Log.info("开始关闭事件线程");
            Events.stop();
            _Log.info("开始关闭心跳线程");
            Heartbeat.stop();
            _Log.info("开始关闭QQ消息接收服务");
            QQNet.stop();
            _Log.info("开始关闭控制台输入线程");
            inputThread.interrupt(); // 中断 inputThread
            try {
                inputThread.join(); // 等待 inputThread 完成
            } catch (InterruptedException e) {
                _Log.error("输入线程正在运行，等待其结束： " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
            _Log.info("开始释放资源");
            DrawConfig.I.dispose();
            _Log.info("Done.");
        }));
    }
    public static void main(String[] args) {
        //初始化基础数据
        DataTable.init();
        $7DTD.init();
        //TestCode.test();
        inputThread.start();
    }
    private static Thread runInputThread() {
        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!isClosing) {
                if (scanner.hasNextLine()) {
                    String userInput = scanner.nextLine();
                    switch (userInput) {
                        case "运行kit" -> QQUsualCommand.runKitExe();
                        case "登录kit" -> $7DTD.loginUser();
                        case "清理服务器" -> ServerCommand.restart();
                        case "重启服务器" -> {
                            if (QQUsualCommand.restartThread.isAlive()) {
                                _Log.warn("重启进程运行中，请勿多次发起");
                            } else {
                                if (System.currentTimeMillis() - QQUsualCommand.wt < 60000) {
                                    _Log.warn("即将完全重启服务器");
                                    QQUsualCommand.restartThread.start();
                                } else {
                                    _Log.warn("如果您想完全重启服务器，请在60秒内再发一次此指令");
                                    QQUsualCommand.wt = System.currentTimeMillis();
                                }
                            }
                        }
                        case "重新加载配置" -> ServerCommand.reloadConfig();
                        case "切换调试模式" -> Config.I.getDatas().isDebug = !Config.I.getDatas().isDebug;

                        case "申请白名单" -> {
                            _Log.info("申请白名单");
                            ACItemsData data = ACItemsConfig.I.get("小小武器5星");
                            if (data == null) {
                                _Log.debug("未找到此白名单");
                            } else {
                                _Log.debug("白名单检测成功");
                                if (ACNet.I.addWhite("EOS_0002test", data.getFormatItems())) {
                                    _Log.debug("白名单添加成功");
                                } else {
                                    _Log.debug("白名单添加失败");
                                }

                            }

                        }
                    }
                }
            }
        });
        inputThread.setDaemon(true);
        return inputThread;
    }

}