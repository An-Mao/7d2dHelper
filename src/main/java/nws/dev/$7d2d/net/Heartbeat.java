package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.core.system._ThreadMonitor;

public class Heartbeat {
    private final ServerCore serverCore;
    private final ServerData serverData;
    private final String chatUrl;
    private final String chatSyncUrl;

    private boolean Start = false;
    private int msgId = -1;
    public final _ThreadMonitor heart;

    public Heartbeat(ServerCore serverCore){
        this.serverCore = serverCore;
        this.serverData = serverCore.serverData;
        this.chatUrl = "http://"+serverData.kitHost() +"/api/chat?admintoken="+serverData.adminToken();
        this.chatSyncUrl = "http://"+serverData.kitHost() +"/api/chat_sync?admintoken="+serverData.adminToken();

        heart = new _ThreadMonitor(() -> {
            $7DTD._Log.info("开始监听服务器消息，心跳频率"+serverData.heartInterval()+"ms");
            int heartInterval = serverData.heartInterval();
            while (Start) {
                try {
                    Thread.sleep(heartInterval);
                    if (heartInterval != serverData.heartInterval()) heartInterval = serverData.heartInterval();
                    if (msgId == -1) setMsgId();
                    KitData.GameMsg msg = getMsg(this.chatSyncUrl+"&count="+serverData.msgCount()+"&firstLine="+msgId+"&timeout="+serverData.heartInterval());
                    if (msg != null) {
                        if (checkMsgData(msg)) {
                            for (KitData.Msg m : msg.list()) {
                                switch (m.msg()) {
                                    case "帮助":
                                        serverCore.help(m);
                                        break;
                                    case "清理":
                                        serverCore.voteClear(m);
                                        break;
                                    case "绑定账号":
                                        serverCore.bind(m);
                                        break;
                                }
                            }
                            setMsgId();
                        }
                    }else {
                        $7DTD._Log.error("拉取服务器信息失败，10秒后重试");
                        heartInterval = 10000;

                    }
                } catch (InterruptedException e) {
                    $7DTD._Log.error("心跳异常",e.getMessage());
                    break;
                }
            }
        },10000);
    }

    public boolean checkMsgData(KitData.GameMsg msg){
        return msg != null && msg.result() == 1 && msg.list() != null && msg.list().length > 0;
    }
    public void setMsgId(){
        KitData.GameMsg msg = getMsg(this.chatUrl+"&count=-1");
        if (checkMsgData(msg)) msgId = msg.list()[0].id() + 1;
    }
    public KitData.GameMsg getMsg(String url) {
        String response = Net.sendGetData(url);
        $7DTD._Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, KitData.GameMsg.class);
    }

    public void start() {
        if (serverData.enableKitHeartbeat()) {
            $7DTD._Log.info("心跳启动");
            Start = true;
            heart.start();
        }else $7DTD._Log.info("心跳已禁用");
    }
    public void stop() {
        Start = false;
    }

}
