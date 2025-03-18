package nws.dev.$7d2d.helper;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.net.Net;
import nws.dev.$7d2d.net.Urls;
import nws.dev.$7d2d.system._Log;

import java.util.HashMap;

public class QQHelper {
    public static void sendGroupMsg(String group, QQData.Msg... msg){
        StringBuilder msgs = new StringBuilder();
        for (QQData.Msg m : msg) {
            if (msgs.isEmpty()) msgs.append(m.toString());
            else msgs.append(",").append(m.toString());
        }
        String data = "{\"group_id\":"+group+",\"message\":["+ msgs +"]}";
        _Log.debug(Urls.qqSendGroupMsg,data);
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Length", String.valueOf(data.length()));
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(Urls.qqSendGroupMsg,"POST",headers,data);
        _Log.debug(response);
    }
    public static void easySendGroupMsg(String group, String msg){
        sendGroupMsg(group, QQData.Msg.create(QQData.MsgType.Text, "text", msg));
    }
    public static void easySendGroupReplyMsg(String group,String id, String msg){
        switch (Config.I.getDatas().qqMsgType){
            case 1 -> sendGroupMsg(group,QQData.Msg.create(QQData.MsgType.Reply, "id", id), QQData.Msg.create(QQData.MsgType.Text, "text", msg));
            case 2 -> easySendGroupMsg(group,msg);
        }
    }
    public static void easySendGroupAtMsg(String group,String qq, String msg){
        sendGroupMsg(group,QQData.Msg.create(QQData.MsgType.At, "steamid", qq), QQData.Msg.create(QQData.MsgType.Text, "text", msg));
    }
}
