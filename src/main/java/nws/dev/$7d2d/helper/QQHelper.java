package nws.dev.$7d2d.helper;

import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.net.Net;
import nws.dev.$7d2d.net.Urls;
import nws.dev.$7d2d.system._Log;

import java.util.HashMap;

public class QQHelper {
    public static void sendGroupMsg(String group, QQData.Msg msg){
        String data = "{\"group_id\":"+group+",\"message\":"+msg.toString()+"}";
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
}
