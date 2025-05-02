package nws.dev.$7d2d;

import nws.dev.$7d2d.command.CommandRegistryNew;
import nws.dev.$7d2d.config.Configs;
import nws.dev.$7d2d.event.Events;
import nws.dev.$7d2d.net.QQNet;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

public class Main {
    public static final String Version = "25.05.0200";
    static {
        System.setProperty("java.awt.headless", "true");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            _Log.info("========程序即将关闭========");
            _Log.info("开始关闭事件线程");
            Events.stop();
            ServerCore.LIST.forEach((s, serverCore) -> {
                _Log.info(s + " 开始关闭心跳线程");
                serverCore.heartbeat.stop();
                _Log.info(s + " 开始释放资源");
                serverCore.drawConfig.dispose();
            });
            _Log.info("开始关闭QQ消息接收服务");
            QQNet.stop();
            _Log.info("Done.");
        }));
    }
    public static void main(String[] args) {
        DataTable.init();
        _Log.info("当前版本：" + Version);
        _Log.info("开始注册命令");
        CommandRegistryNew.registerCommands("nws.dev.$7d2d");
        _Log.info("已注册【"+CommandRegistryNew.getCommandMap().size()+"】命令");
        _Log.info("开始初始化");
        Configs.init();
        $7DTD.init();
        //TestCode.test();
    }

}