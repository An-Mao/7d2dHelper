package nws.dev.$7d2d;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.event.Events;
import nws.dev.$7d2d.net.QQNet;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;

public class $7DTD {
    public static void init() {
        try {
            loginUser();
            Events.event.start();
            _Log.info("开始启动对" + Config.I.listenPort() + "端口消息的监听");
            QQNet.listen();
        } catch (IOException e) {
            _Log.error(e.getMessage());
        }
    }

    public static void loginUser() {
        ServerCore.LIST.forEach((s, serverList) -> serverList.login());
    }

}
