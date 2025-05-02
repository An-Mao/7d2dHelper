package nws.dev.$7d2d.event;

import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;
import nws.dev.$7d2d.system._ThreadMonitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Events {
    public static int day = 0;
    private static boolean run = true;
    public static final _ThreadMonitor event = new _ThreadMonitor(() -> {
        while (run) {
            try {
                Thread.sleep(3000);
                boolean[] refresh = {false};
                if (day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    _Log.info("刷新事件");
                    day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    refresh[0] = true;
                    _Log.SetLogFileName();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String k = sdf.format(new Date());
                ServerCore.LIST.forEach((s, serverCore) -> {
                    if (refresh[0]) {
                        serverCore.no.clear();
                        serverCore.kitNet.refreshMap();
                    }
                    String v = serverCore.events.get(k);
                    if ( v != null && !v.isEmpty() && !serverCore.no.contains(k)) {
                        serverCore.no.add(k);
                        serverCore.serverData.qqGroup().forEach(s1 -> QQHelper.easySendGroupMsg(s1, v));
                    }
                });
            } catch (InterruptedException e) {
                _Log.error("时钟异常",e.getMessage());
                break;
            }
        }
    },60000);
    public static void stop(){
        run = false;
    }
}
