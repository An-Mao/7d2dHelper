package dev.anye.sdtd.helper.helper;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.net.Net;
import dev.anye.sdtd.helper.net.Urls;
import dev.anye.sdtd.helper.server.ServerCore;

import java.util.Arrays;
import java.util.HashMap;

public class QQHelper {
    public static final String data = """
                {
                "group_id":<group>,
                "message":
                    [
                        {
                            "type":"image",
                            "data":
                                {
                                    "file":"<file>",
                                    "subType":0
                                }
                        }
                    ]
                }
                """;
    public static void sendGroupMsg(String group, QQData.Msg... msg){
        StringBuilder msgs = new StringBuilder();
        for (QQData.Msg m : msg) {
            if (msgs.isEmpty()) msgs.append(m.toString());
            else msgs.append(",").append(m.toString());
        }
        String data = "{\"group_id\":"+group+",\"message\":["+ msgs +"]}";
        $7DTD._Log.debug(Urls.qqSendGroupMsg,data);
        sendMsg(Urls.qqSendGroupMsg,data);
    }
    public static void sendPrivateMsg(String qq, QQData.Msg... msg){
        $7DTD._Log.debug("尝试发送私聊消息 qq:" + qq + " msg:" + Arrays.toString(msg));
        StringBuilder msgs = new StringBuilder();
        for (QQData.Msg m : msg) {
            if (msgs.isEmpty()) msgs.append(m.toString());
            else msgs.append(",").append(m.toString());
        }
        String data = "{\"user_id\":"+qq+",\"message\":["+ msgs +"]}";
        $7DTD._Log.debug(Urls.qqSendPrivateMsg,data);
        sendMsg(Urls.qqSendPrivateMsg,data);
    }
    public static void sendMsg(String url,String data){
        $7DTD._Log.debug(data);
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Length", String.valueOf(data.length()));
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url,"POST",headers,data);
        $7DTD._Log.debug(response);
    }



    public static void easySendGroupMsg(String group, String msg){
        sendGroupMsg(group, QQData.Msg.create(QQData.MsgType.Text, "text", msg));
    }
    public static void easySendGroupReplyMsg(ServerCore serverCore, String group, String id, String msg){
        switch (serverCore.serverData.qqMsgType()){
            case 1 -> sendGroupMsg(group,QQData.Msg.create(QQData.MsgType.Reply, "id", id), QQData.Msg.create(QQData.MsgType.Text, "text", msg));
            case 2 -> easySendGroupMsg(group,msg);
        }
    }
    public static void easySendGroupAtMsg(String group,String qq, String msg){
        sendGroupMsg(group,QQData.Msg.create(QQData.MsgType.At, "qq", qq), QQData.Msg.create(QQData.MsgType.Text, "text", msg));
    }
    public static void easySendMsg(String qq, String msg){
        $7DTD._Log.debug(Urls.qqSendPrivateMsg);
        sendPrivateMsg(qq, QQData.Msg.create(QQData.MsgType.Text, "text", msg));
    }
    public static void easySendReplyMsg(ServerCore serverCore, String qq,String id, String msg){
        $7DTD._Log.debug(String.valueOf(serverCore.serverData.qqMsgType()));
        switch (serverCore.serverData.qqMsgType()){
            case 1 -> {
                $7DTD._Log.debug("尝试发送回复消息 qq:" + qq + " id:" + id + " msg:" + msg);
                sendPrivateMsg(qq,QQData.Msg.create(QQData.MsgType.Reply, "id", id), QQData.Msg.create(QQData.MsgType.Text, "text", msg));
            }
            case 2 -> easySendMsg(qq,msg);
        }
    }

    public static String replaceUnescapedNewlines(String str) {
        return str.replaceAll("(?<!\\\\)\\\\n", "\\\\\\\\n");
    }
}
