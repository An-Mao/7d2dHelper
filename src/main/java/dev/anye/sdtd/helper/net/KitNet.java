package dev.anye.sdtd.helper.net;

import com.google.gson.Gson;
import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.data.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class KitNet {
    private final Gson gson = new Gson();
    public final ServerData serverData;
    public final String url ;
    private final String gsListUrl;
    private final String urlUserList;
    private final String urlActUnban;
    private final String urlMapName;
    private final String urlActKick;
    private final String urlActBan;

    private final HashMap<String, String> headers;

    public Web.Kit webUser = new Web.Kit();

    public KitNet(ServerData serverData){
        this.serverData = serverData;
        this.url = "http://"+serverData.kitHost() +"/";
        this.gsListUrl = "http://"+ serverData.kitHost() +"/cgi/gs_list";
        this.urlUserList = "http://"+ serverData.kitHost() +"/cgi/user_list";
        this.urlActUnban = "http://"+ serverData.kitHost() +"/cgi/user_act_unban";
        this.urlMapName = "http://"+ serverData.kitHost() +"/cgi/mapname_set";
        this.urlActKick = "http://"+ serverData.kitHost() +"/cgi/user_act_kick";
        this.urlActBan = "http://"+ serverData.kitHost() +"/cgi/user_act_ban";
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
    }
    public boolean loginUser() {
        HashMap<String, String> headers = getHeaders();

        String login = "{\"username\":\""+ serverData.kitUsername() +"\",\"password\":\""+ serverData.kitPassword() +"\"}";
        headers.put("Content-Length", String.valueOf(login.length()));
        headers.put("Host",serverData.kitHost());
        headers.put("Origin","http://"+serverData.kitHost());
        headers.put("Referer","http://"+serverData.kitHost() +"/login");
        String response = Net.sendData(url+"cgi/login","POST",headers,login);
        webUser = gson.fromJson(response, Web.Kit.class);
        return webUser != null && webUser.result.equals("1");
    }
    public void restart(String time){
        String response = Net.sendData(url + "cgi/gs_act_autoreboot","POST",headers,"{\"accessToken\":\""+getToken()+"\",\"uuid\":\"595768906\",\"enable\":1,\"keepalive\":1,\"type\":\"1\",\"duration\":\""+time+"\",\"keepalive_timeout\":\"600\"}");
        $7DTD._Log.debug(response);
    }

    public KitData.BanUser getBan(String sid){
        if (sid.contains("_")) sid = sid.split("_")[1];
        String response = Net.sendData(url + "cgi/user_ban","POST",headers,"{\"q\":\""+sid+"\",\"perPage\":\"25\",\"page\":\"1\",\"accessToken\":\""+getToken()+"\"}");
        $7DTD._Log.debug(response);
        KitData.BanInfo ban = gson.fromJson(response, KitData.BanInfo.class);
        if (ban != null && ban.result() == 1 && ban.users() != null && ban.users().length > 0) {
            return ban.users()[0];
        }
        return null;
    }

    public void stopNet() {
        String response = Net.sendData(url + "cgi/shutdown","POST",headers,"{\"accessToken\":\""+getToken()+"\"}");
        $7DTD._Log.debug(response);
    }
    public void startServer(){
        String response = Net.sendData(url + "cgi/gs_startdefault","POST",headers,"{\"accessToken\":\""+getToken()+"\"}");
        $7DTD._Log.debug(response);
    }
    public void refreshMap(){
        String response = Net.sendData(urlMapName,"POST",headers,"{\"accessToken\":\""+getToken()+"\" ,\"mapname\":\""+getMapName()+"\"}");
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

    public boolean ban(String steamId,long time){
        PostData postData = new PostData();
        postData.add("accessToken",getToken());
        postData.add("steamid",formatSteamId(steamId));
        postData.add("banuntil", String.valueOf(time));
        postData.add("reason","qq command");
        String response = Net.sendData(this.urlActBan,"POST",headers,postData.toJson());
        $7DTD._Log.debug(response);
        return checkResult(response);

    }
    public boolean unBan(String steamID) {
        PostData postData = new PostData();
        postData.add("accessToken",getToken());
        postData.add("steamid",formatSteamId(steamID));
        String response = Net.sendData(this.urlActUnban,"POST",headers,postData.toJson());
        $7DTD._Log.debug(response);
        return checkResult(response);
    }
    public boolean kick(String steamID){
        PostData postData = new PostData();
        postData.add("accessToken",getToken());
        postData.add("steamid",formatSteamId(steamID));
        postData.add("reason","command");
        String response = Net.sendData(this.urlActKick,"POST",headers,postData.toJson());
        $7DTD._Log.debug(response);
        return checkResult(response);
    }
    public PlayerInfoData getBagItems(String steamID){
        PostData postData = new PostData();
        postData.add("accessToken",getToken());
        postData.add("steamid",steamID);
        $7DTD._Log.debug(postData.toJson());
        String response = Net.sendData(url + "cgi/user_detail","POST",headers,postData.toJson());
        //_Log.debug(response);
        return gson.fromJson(response, PlayerInfoData.class);
    }
    public boolean removeBagItems(String steamID,String index){
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
        return checkResult(gson.fromJson(res, Web.Result.class));
    }
    public boolean checkResult(Web.Result res) {
        return res != null && res.result == 1;
    }
    public List<KitData.Player> getPlayers(String key){
        List<KitData.Player> playerList = new ArrayList<>();
        int index = 1,limit = 25;
        PostData postData = getPlayerPage(index,key,limit);
        String response = Net.sendData(urlUserList,"POST",headers,postData.toJson());
        if (response.isEmpty()) return playerList;
        $7DTD._Log.debug(response);
        KitData.Players players = gson.fromJson(response,KitData.Players.class);

        if (players != null && players.result() == 1 && !players.users().isEmpty()) playerList.addAll(players.users());

        while (players != null && players.result() == 1 && players.users().size() == limit){
            index ++;
            postData = getPlayerPage(index,key,limit);
            response = Net.sendData(urlUserList,"POST",headers,postData.toJson());
            players = gson.fromJson(response,KitData.Players.class);
            if (players != null && players.result() == 1 && !players.users().isEmpty()) playerList.addAll(players.users());
        }
        return playerList;
    }
    public @Nullable KitData.Player getPlayerWithSteamId(String sid){
        AtomicReference<KitData.Player> p = new AtomicReference<>(null);
        final String fsid = formatSteamId(sid);
        getPlayers(fsid).forEach(player -> {
            if (p.get() == null && player.steamid().equals(fsid)) p.set(player);
        });
        return p.get();
    }
    public PostData getPlayerPage(int page,String key,int limit){
        PostData postData = new PostData();
        postData.add("accessToken",getToken());
        postData.add("page", String.valueOf(page));
        postData.add("perPage", String.valueOf(limit));
        postData.add("q",key);
        return postData;
    }
    public String formatSteamId(String steamID){
        return steamID.contains("_") ? steamID.split("_")[1] : steamID;
    }
}
