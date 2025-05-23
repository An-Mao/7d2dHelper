package dev.anye.sdtd.helper;

import com.sun.jna.platform.win32.Version;
import dev.anye.sdtd.helper.command.CommandRegistryNew;
import dev.anye.sdtd.helper.config.Configs;
import dev.anye.sdtd.helper.event.Events;
import dev.anye.sdtd.helper.net.QQNet;
import dev.anye.sdtd.helper.server.ServerList;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static final String Version = getVersion();
    static {
        System.setProperty("java.awt.headless", "true");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            $7DTD._Log.info("========程序即将关闭========");
            $7DTD._Log.info("开始关闭事件线程");
            Events.stop();
            ServerList.LIST.forEach((s, serverCore) -> {
                $7DTD._Log.info(s + " 开始关闭心跳线程");
                serverCore.heartbeat.stop();
                $7DTD._Log.info(s + " 开始释放资源");
                serverCore.drawConfig.dispose();
            });
            $7DTD._Log.info("开始关闭QQ消息接收服务");
            QQNet.stop();
            $7DTD._Log.info("Done.");
        }));
    }
    public static void main(String[] args) {
        //System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        DataTable.init();
        $7DTD._Log.info("当前版本：" + Version);
        $7DTD._Log.info("开始注册命令");
        CommandRegistryNew.registerCommands("dev.anye.sdtd.helper.command");
        $7DTD._Log.info("已注册【"+CommandRegistryNew.getCommands().size()+"】命令");
        $7DTD._Log.info("开始初始化");
        Configs.init();
        $7DTD.init();
        //TestCode.test();
    }


    public static String getVersion() {
        try (InputStream input = Version.class.getClassLoader().getResourceAsStream("META-INF/maven/com.example/my-project/pom.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                return prop.getProperty("version");
            } else {
                return "Development Version"; // 或者其他默认值
            }
        } catch (IOException e) {
            return "Error Reading Version"; // 或者其他错误处理
        }
    }

}