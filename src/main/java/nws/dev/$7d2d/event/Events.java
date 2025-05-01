package nws.dev.$7d2d.event;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.EventsConfig;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.KitNet;
import nws.dev.$7d2d.system._Log;
import nws.dev.$7d2d.system._ThreadMonitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Events {
    public static final List<String> no = new ArrayList<>();
    public static int day = 0;
    private static boolean run = true;
    public static final _ThreadMonitor event = new _ThreadMonitor(() -> {
        while (run) {
            try {
                Thread.sleep(3000);
                if (day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    _Log.info("刷新事件");
                    day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    no.clear();
                    KitNet.refreshMap();
                    _Log.SetLogFileName();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String k = sdf.format(new Date());
                String v = EventsConfig.I.get(k);
                if ( v != null && !v.isEmpty() && !no.contains(k)) {
                    no.add(k);
                    Config.I.getDatas().qqGroup.forEach(s -> QQHelper.easySendGroupMsg(s, v));
                }
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
