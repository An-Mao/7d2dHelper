package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.data.Web;
import nws.dev.$7d2d.system._Log;

import java.util.HashMap;

public class KitNet {
    public static final String url = "http://"+Config.I.getDatas().kitHost +"/";
    public static Web.Kit webUser = new Web.Kit();

    public static boolean loginUser() {
        HashMap<String, String> headers = getHeaders();

        String login = "{\"username\":\""+ Config.I.getDatas().kitUsername +"\",\"password\":\""+ Config.I.getDatas().kitPassword +"\"}";
        headers.put("Content-Length", String.valueOf(login.length()));
        headers.put("Host",Config.I.getDatas().kitHost);
        headers.put("Origin","http://"+Config.I.getDatas().kitHost);
        headers.put("Referer","http://"+Config.I.getDatas().kitHost +"/login");
        String response = Net.sendData(url+"cgi/login","POST",headers,login);
        Gson gson = new Gson();
        webUser = gson.fromJson(response, Web.Kit.class);
        return webUser != null && webUser.result.equals("1");
    }
    public static void restart(String time){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/gs_act_autoreboot","POST",headers,"{\"accessToken\":\""+getToken()+"\",\"uuid\":\"595768906\",\"enable\":1,\"keepalive\":1,\"type\":\"1\",\"duration\":\""+time+"\",\"keepalive_timeout\":\"600\"}");
        _Log.debug(response);
    }

    public static KitData.BanUser getBan(String sid){
        if (sid.contains("_")) sid = sid.split("_")[1];
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/user_ban","POST",headers,"{\"q\":\""+sid+"\",\"perPage\":\"25\",\"page\":\"1\",\"accessToken\":\""+getToken()+"\"}");
        _Log.debug(response);
        Gson gson = new Gson();
        KitData.BanInfo ban = gson.fromJson(response, KitData.BanInfo.class);
        if (ban != null && ban.result() == 1 && ban.users() != null && ban.users().length > 0) {
            return ban.users()[0];
        }
        return null;
    }



    public static void stopNet() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/shutdown","POST",headers,"{\"accessToken\":\""+getToken()+"\"}");
        _Log.debug(response);
    }

    public static void startServer(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/gs_startdefault","POST",headers,"{\"accessToken\":\""+getToken()+"\"}");
        _Log.debug(response);
    }

    public static KitData.GsList getGsList(){
        _Log.debug(Urls.gsListUrl);
        String data = "{\"accessToken\":\""+ getToken()+"\"}";
        HashMap<String,String> headers = getHeaders();
        headers.put("Content-Length", String.valueOf(data.length()));
        headers.put("Host", Config.I.getDatas().kitHost);
        headers.put("Origin","http://"+Config.I.getDatas().kitHost);
        headers.put("Referer","http://"+Config.I.getDatas().kitHost +"/gs");
        String response = Net.sendData(Urls.gsListUrl,"POST",headers,data);
        _Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, KitData.GsList.class);
    }
    public static String getToken() {
        if (webUser == null || !checkToken()){
            _Log.warn("accessToken失效，尝试重新登录");
            if (!loginUser()) return "";
        }
        return webUser.accessToken;
    }
    public static boolean checkToken() {
        if (webUser == null) return false;
        HashMap<String, String> headers = getHeaders();
        String data = "{\"accessToken\":\""+ webUser.accessToken + "\"}";
        headers.put("Content-Length", String.valueOf(data.length()));
        headers.put("Host",Config.I.getDatas().kitHost);
        headers.put("Origin","http://"+Config.I.getDatas().kitHost);
        headers.put("Referer","http://"+Config.I.getDatas().kitHost +"/");
        String response = Net.sendData(url + "cgi/session","POST",headers,data);
        return BotNet.checkResult(response);
    }
    public static HashMap<String, String> getHeaders(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Connection","keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36;");
        return headers;
    }

    public static boolean unBan(String steamID) {
        if (steamID.contains("_")) steamID = steamID.split("_")[1];
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/user_act_unban","POST",headers,"{\"steamid\":\""+steamID+"\",\"accessToken\":\""+getToken()+"\"}");
        _Log.debug(response);
        return BotNet.checkResult(response);
    }




    public static boolean kick(String steamID){
        if (steamID.contains("_")) steamID = steamID.split("_")[1];
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/user_act_kick","POST",headers,"{\"steamid\":\""+steamID+"\",\"accessToken\":\""+getToken()+"\",\"reason\":\"command\"}");
        _Log.debug(response);
        return BotNet.checkResult(response);
    }
}
