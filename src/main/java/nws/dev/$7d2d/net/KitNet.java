package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.data.*;

import java.util.Calendar;
import java.util.HashMap;

public class KitNet {
    public final ServerData serverData;
    public final String url ;
    private final String gsListUrl;
    public Web.Kit webUser = new Web.Kit();

    public KitNet(ServerData serverData){
        this.serverData = serverData;
        this.url = "http://"+serverData.kitHost() +"/";
        this.gsListUrl = "http://"+ serverData.kitHost() +"/cgi/gs_list";
    }
    public boolean loginUser() {
        HashMap<String, String> headers = getHeaders();

        String login = "{\"username\":\""+ serverData.kitUsername() +"\",\"password\":\""+ serverData.kitPassword() +"\"}";
        headers.put("Content-Length", String.valueOf(login.length()));
        headers.put("Host",serverData.kitHost());
        headers.put("Origin","http://"+serverData.kitHost());
        headers.put("Referer","http://"+serverData.kitHost() +"/login");
        String response = Net.sendData(url+"cgi/login","POST",headers,login);
        Gson gson = new Gson();
        webUser = gson.fromJson(response, Web.Kit.class);
        return webUser != null && webUser.result.equals("1");
    }
    public void restart(String time){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/gs_act_autoreboot","POST",headers,"{\"accessToken\":\""+getToken()+"\",\"uuid\":\"595768906\",\"enable\":1,\"keepalive\":1,\"type\":\"1\",\"duration\":\""+time+"\",\"keepalive_timeout\":\"600\"}");
        $7DTD._Log.debug(response);
    }

    public KitData.BanUser getBan(String sid){
        if (sid.contains("_")) sid = sid.split("_")[1];
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/user_ban","POST",headers,"{\"q\":\""+sid+"\",\"perPage\":\"25\",\"page\":\"1\",\"accessToken\":\""+getToken()+"\"}");
        $7DTD._Log.debug(response);
        Gson gson = new Gson();
        KitData.BanInfo ban = gson.fromJson(response, KitData.BanInfo.class);
        if (ban != null && ban.result() == 1 && ban.users() != null && ban.users().length > 0) {
            return ban.users()[0];
        }
        return null;
    }

    public String formatSteamId(String sid){

        if (sid.contains("_")) sid = sid.split("_")[1];
        return sid;

    }


    public void stopNet() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/shutdown","POST",headers,"{\"accessToken\":\""+getToken()+"\"}");
        $7DTD._Log.debug(response);
    }

    public void startServer(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/gs_startdefault","POST",headers,"{\"accessToken\":\""+getToken()+"\"}");
        $7DTD._Log.debug(response);
    }
    public void refreshMap(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/mapname_set","POST",headers,"{\"accessToken\":\""+getToken()+"\" ,\"mapname\":\""+getMapName()+"\"}");
        $7DTD._Log.debug(response);
    }
    public String getMapName(){
        Calendar rightNow = Calendar. getInstance();
        String name = serverData.mapName().replace("<Month>", String.valueOf(rightNow.get(Calendar.MONTH) + 1));
        name = name.replace("<Day>", String.valueOf(rightNow.get(Calendar.DAY_OF_MONTH)));
        name = name.replace("<Week>", String.valueOf(rightNow.get(Calendar.WEEK_OF_MONTH)));
        name = name.replace("<WeekDay>", String.valueOf(rightNow.get(Calendar.DAY_OF_WEEK)));
        name = name.replace("<Year>", String.valueOf(rightNow.get(Calendar.YEAR)));
        name = name.replace("<Hour>", String.valueOf(rightNow.get(Calendar.HOUR)));
        return name.replace("<Minute>", String.valueOf(rightNow.get(Calendar.MINUTE)));
    }
    public KitData.GsList getGsList(){
        $7DTD._Log.debug(this.gsListUrl);
        String data = "{\"accessToken\":\""+ getToken()+"\"}";
        HashMap<String,String> headers = getHeaders();
        headers.put("Content-Length", String.valueOf(data.length()));
        headers.put("Host", serverData.kitHost());
        headers.put("Origin","http://"+serverData.kitHost());
        headers.put("Referer","http://"+serverData.kitHost() +"/gs");
        String response = Net.sendData(this.gsListUrl,"POST",headers,data);
        $7DTD._Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, KitData.GsList.class);
    }
    public String getToken() {
        if (webUser == null || !checkToken()){
            $7DTD._Log.warn("accessToken失效，尝试重新登录");
            if (!loginUser()) return "";
        }
        return webUser.accessToken;
    }
    public boolean checkToken() {
        if (webUser == null) return false;
        HashMap<String, String> headers = getHeaders();
        String data = "{\"accessToken\":\""+ webUser.accessToken + "\"}";
        headers.put("Content-Length", String.valueOf(data.length()));
        headers.put("Host",serverData.kitHost());
        headers.put("Origin","http://"+serverData.kitHost());
        headers.put("Referer","http://"+serverData.kitHost() +"/");
        String response = Net.sendData(url + "cgi/session","POST",headers,data);
        return checkResult(response);
    }
    public HashMap<String, String> getHeaders(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Connection","keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36;");
        return headers;
    }

    public boolean unBan(String steamID) {
        if (steamID.contains("_")) steamID = steamID.split("_")[1];
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/user_act_unban","POST",headers,"{\"steamid\":\""+steamID+"\",\"accessToken\":\""+getToken()+"\"}");
        $7DTD._Log.debug(response);
        return checkResult(response);
    }




    public boolean kick(String steamID){
        if (steamID.contains("_")) steamID = steamID.split("_")[1];
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String response = Net.sendData(url + "cgi/user_act_kick","POST",headers,"{\"steamid\":\""+steamID+"\",\"accessToken\":\""+getToken()+"\",\"reason\":\"command\"}");
        $7DTD._Log.debug(response);
        return checkResult(response);
    }







    public PlayerInfoData getBagItems(String steamID){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        PostData postData = new PostData();
        postData.add("accessToken",getToken());
        postData.add("steamid",steamID);
        $7DTD._Log.debug(postData.toJson());
        String response = Net.sendData(url + "cgi/user_detail","POST",headers,postData.toJson());
        //_Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, PlayerInfoData.class);
    }
    public boolean removeBagItems(String steamID,String index){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        PostData postData = new PostData();
        postData.add("accessToken",getToken());
        postData.add("steamid",steamID);
        postData.add("loc",index);
        $7DTD._Log.debug(postData.toJson());
        String response = Net.sendData(url + "cgi/user_act_removeitem_bag","POST",headers,postData.toJson());
        //_Log.debug(response);
        return checkResult(response);
    }

    public boolean checkResult(String res) {
        Gson gson = new Gson();
        return checkResult(gson.fromJson(res, Web.Result.class));
    }
    public boolean checkResult(Web.Result res) {
        return res != null && res.result == 1;
    }
}
