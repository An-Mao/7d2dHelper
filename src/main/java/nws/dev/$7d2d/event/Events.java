package nws.dev.$7d2d.event;

import nws.dev.$7d2d.command.ServerCommand;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.EventsConfig;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.KitNet;
import nws.dev.$7d2d.net.Urls;
import nws.dev.$7d2d.system._Log;
import nws.dev.$7d2d.system._ThreadMonitor;

import java.text.SimpleDateFormat;
import java.util.*;

public class Events {
    public static final List<String> no = new ArrayList<>();
    public static int day = 0;
    public static final _ThreadMonitor event = new _ThreadMonitor(() -> {
        while (true) {
            try {
                Thread.sleep(3000);
                if (day != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    _Log.info("刷新事件");
                    day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    no.clear();
                    KitNet.refreshMap();
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
}
