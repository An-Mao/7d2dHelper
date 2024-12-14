package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import nws.dev.$7d2d.command.ServerCommand;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.system._Log;
import nws.dev.$7d2d.system._ThreadMonitor;

public class Heartbeat {
    private static boolean Start = false;


    private static int msgId = -1;


    public static final _ThreadMonitor heart = new _ThreadMonitor(() -> {
        _Log.info("开始监听服务器消息，心跳频率"+Config.I.getDatas().heartInterval+"ms");
        int heartInterval = Config.I.getDatas().heartInterval;
        while (Start) {
            try {
                Thread.sleep(heartInterval);
                if (heartInterval != Config.I.getDatas().heartInterval) heartInterval = Config.I.getDatas().heartInterval;
                if (msgId == -1) setMsgId();
                KitData.GameMsg msg = getMsg(Urls.chatSyncUrl+"&count="+Config.I.getDatas().msgCount+"&firstLine="+msgId+"&timeout="+Config.I.getDatas().heartInterval);
                if (msg != null) {
                    if (checkMsgData(msg)) {
                        for (KitData.Msg m : msg.list()) {
                            switch (m.msg()) {
                                case "帮助":
                                    ServerCommand.help(m);
                                    break;
                                case "清理":
                                    ServerCommand.voteClear(m);
                                    break;
                                case "绑定账号":
                                    ServerCommand.bind(m);
                                    break;
                            }
                        }
                        setMsgId();
                    }
                }else {
                    _Log.error("拉取服务器信息失败，60秒后重试");
                    heartInterval = 60000;

                }
            } catch (InterruptedException e) {
                _Log.error("心跳异常",e.getMessage());
                break;
            }
        }
    },60000);
    public static boolean checkMsgData(KitData.GameMsg msg){
        return msg != null && msg.result() == 1 && msg.list() != null && msg.list().length > 0;
    }
    public static void setMsgId(){
        KitData.GameMsg msg = getMsg(Urls.chatUrl+"&count=-1");
        if (checkMsgData(msg)) msgId = msg.list()[0].id() + 1;
    }
    public static KitData.GameMsg getMsg(String url) {
        String response = Net.sendGetData(url);
        _Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, KitData.GameMsg.class);
    }

    public static void start() {
        _Log.info("心跳启动");
        Start = true;
        heart.start();
    }
    public static void stop() {
        Start = false;
    }

}
