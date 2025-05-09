package nws.dev.$7d2d;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.event.Events;
import nws.dev.$7d2d.net.QQNet;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.core.system._Log;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class $7DTD {
    public static final  _Log _Log = new _Log(getLogFileName());
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
    public static void refresh() {
        _Log.setLogFile(getLogFileName());
    }

    private static String getLogFileName(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm");
        String formattedDateTime = now.format(formatter);
        return DataTable.LogDir + "/"+formattedDateTime+".log";
    }
    public static void loginUser() {
        ServerCore.LIST.forEach((s, serverList) -> serverList.login());
    }

}
