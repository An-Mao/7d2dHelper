package dev.anye.sdtd.helper;

import dev.anye.core.system._Log;
import dev.anye.sdtd.helper.config.Config;
import dev.anye.sdtd.helper.event.Events;
import dev.anye.sdtd.helper.net.QQNet;
import dev.anye.sdtd.helper.server.ServerList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class $7DTD {
    public static final  _Log _Log = new _Log(getLogFileName(),Config.I.getDatas().logColor());
    static {
        _Log.Debug = Config.I.getDatas().isDebug();
    }
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
        ServerList.LIST.forEach((s, serverList) -> serverList.login());
    }

}
