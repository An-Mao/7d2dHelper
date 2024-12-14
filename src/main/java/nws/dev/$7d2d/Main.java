package nws.dev.$7d2d;

import nws.dev.$7d2d.command.ServerCommand;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.*;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final String Version = "24.12.1303";
    public static void main(String[] args) throws IOException {
        _Log.info("当前版本："+Version);
        loginUser();
        _Log.info("开始启动对"+ Config.I.getDatas().listenPort +"端口消息的监听");
        QQNet.listen();
        Heartbeat.start();
        Thread inputThread = runInputThread();
        inputThread.start();
    }

    private static void loginUser(){
        _Log.info("开始登录Ket");
        if (KitNet.loginUser()){
            _Log.info("Ket登录成功：accessToken="+ KitNet.getToken());
        }else {
            _Log.error("Ket登录失败");
        }
        _Log.info("开始登录Bot");
        if (BotNet.loginUser()){
            _Log.info("Bot登录成功：session_key="+ BotNet.getToken());
        }else {
            _Log.error("Bot登录失败");
        }
    }
    private static Thread runInputThread() {
        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.hasNextLine()) {
                    String userInput = scanner.nextLine();
                    switch (userInput){
                        case "运行kit" -> QQNet.runKitExe();
                        case "登录kit" -> loginUser();
                        case "清理服务器" -> ServerCommand.restart();
                        case "重启服务器" -> {
                            if (QQNet.restartThread.isAlive()){
                                _Log.warn("重启进程运行中，请勿多次发起");
                            }else {
                                if (System.currentTimeMillis() - QQNet.wt < 60000) {
                                    _Log.warn( "即将完全重启服务器");
                                    QQNet.restartThread.start();
                                }else {
                                    _Log.warn( "如果您想完全重启服务器，请在60秒内再发一次此指令");
                                    QQNet.wt = System.currentTimeMillis();
                                }
                            }
                        }
                        case "重新加载配置" -> ServerCommand.reloadConfig();
                        case "切换调试模式" -> Config.I.getDatas().isDebug = !Config.I.getDatas().isDebug;
                    }
                }
            }
        });
        inputThread.setDaemon(true);
        return inputThread;
    }

}