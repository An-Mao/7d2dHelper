package nws.dev.$7d2d.helper;

import com.google.gson.Gson;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.net.BotNet;
import nws.dev.$7d2d.net.Net;
import nws.dev.$7d2d.net.Urls;
import nws.dev.$7d2d.system._Log;

public class ServerHelper {
    public static void sendChatMsg(String... msgs){
        //_Log.info();
        for (String msg:msgs) BotNet.sendPublicMsg(msg);
    }
    public static void sendServerCommand(String msg){
        _Log.debug(msg);
        String c = Urls.commandUrl+"&command="+ Net.urlEncode(msg);
        _Log.debug(c);
        String response = Net.sendGetData(c);
        _Log.debug(response);
        /*
        Gson gson = new Gson();
        KitData.Command res = gson.fromJson(response, KitData.Command.class);
        _Log.debug(res.result());

         */
    }
    public static KitData.PlayerInfo[] getPlayerList(){
        String response = Net.sendGetData(Urls.onlinePlayerUrl);
        _Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, KitData.PlayerInfo[].class);
    }

}
