package dev.anye.sdtd.helper.event;

import dev.anye.core.system._ThreadMonitor;
import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.helper.QQHelper;
import dev.anye.sdtd.helper.server.ServerList;

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
                    $7DTD._Log.info("刷新事件");
                    day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    refresh[0] = true;
                    $7DTD.refresh();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String k = sdf.format(new Date());
                ServerList.LIST.forEach((s, serverCore) -> {
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
                $7DTD._Log.error("时钟异常",e.getMessage());
                break;
            }
        }
    },60000);
    public static void stop(){
        run = false;
    }
}
