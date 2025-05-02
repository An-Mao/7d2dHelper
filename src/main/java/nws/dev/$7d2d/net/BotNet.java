package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.RewardData;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.$7d2d.data.Web;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

import java.util.ArrayList;
import java.util.List;

public class BotNet {
    private final ServerCore serverCore;
    private final ServerData serverData;

    private final String rootUrl;
    private final String loginUrl;
    private final String pointUrl;
    private final String giveItemUrl;
    private final String localUrl;

    private static Web.Bot Use = new Web.Bot();
    public BotNet(ServerCore serverCore){
        this.serverCore = serverCore;
        this.serverData = serverCore.serverData;
        this.rootUrl = "http://"+ serverData.botHost() +"/";
        this.loginUrl = rootUrl +"api/login?username="+serverData.botUsername() +"&password="+serverData.botPassword();
        this.pointUrl = rootUrl +"api/action_pointadd?key=";
        this.giveItemUrl = rootUrl +"api/action_giveitem?key=";
        this.localUrl = rootUrl + "api/localizations?key=";

    }


    public boolean loginUser() {
        String response = Net.sendGetData(this.loginUrl);
        _Log.debug("bot 登录结果："+response);
        Gson gson = new Gson();
        Use = gson.fromJson(response, Web.Bot.class);
        return Use != null && Use.result == 1;
    }

    public boolean sendPoint(String user, int count) {
        String url = this.pointUrl + getToken() +"&p="+user+"&count="+count+"&isnotice=1";
        String response = Net.sendGetData(url);
        _Log.debug(response);
        return checkResult(response);
    }
    public boolean giveItem(String user, String name, int count, int quality) {
        String url = this.giveItemUrl+ getToken() +"&p="+user+"&name="+Net.urlEncode(name)+"&count="+count+"&quality="+quality;
        String response = Net.sendGetData(url);
        _Log.debug(response);
        return checkResult(response);
    }
    public boolean killPlayer(String user) {
        String url = rootUrl +"api/action_kill?key="+ getToken() +"&p="+user;
        String response = Net.sendGetData(url);
        _Log.debug(response);
        return checkResult(response);
    }



    public String getItemName(String itemId) {
        int page = 1,limit = 10;
        String name = "";
        while (name.isEmpty()) {
            String url = this.localUrl + getToken() + "&page=" + page + "&limit=" + limit + "&keyword=" + Net.urlEncode(itemId);
            String response = Net.sendGetData(url);
            _Log.debug(response);
            Gson gson = new Gson();
            BotData.ItemList res = gson.fromJson(response, BotData.ItemList.class);
            for (BotData.ItemInfo item : res.data()) {
                if (item.itemid().equals(itemId)) name = item.itemname();
            }
            if (page * limit >= res.count()) break;
            else page++;
        }
        if(name.isEmpty()) name = itemId;
        return name;
    }


    public String getToken() {
        if (Use == null || Use.session_key == null || Use.session_key.isEmpty() || !checkLogin()) {
            _Log.warn("session_key失效，尝试重新登录");
            if (!loginUser()) return "";
        }
        return Use.session_key;
    }
    public boolean checkLogin() {
        String response = Net.sendGetData(rootUrl +"api/checksession?key="+ Use.session_key);
        _Log.debug(response);
        return checkResult(response);
    }

    public BotData.UserInfo getUserByName(String name) {
        List<BotData.UserInfo> list = getUserList();
        for (BotData.UserInfo info : list) {
            if (info.playername().equals(name)) return info;
        }
        return null;
    }
    public BotData.UserInfo getUserBySteamID(String name) {
        List<BotData.UserInfo> list = getUserList();
        for (BotData.UserInfo info : list) {
            if (info.platformid().equals(name)) return info;
        }
        return null;
    }
    public List<BotData.UserInfo> getUserList() {
        int page = 1,limit = 50;
        boolean isAll = false;
        List<BotData.UserInfo> list = new ArrayList<>();
        while (!isAll) {
            String url = rootUrl +"api/point?key="+getToken()+"&page="+page+"&limit="+limit;
            String response = Net.sendGetData(url);
            _Log.debug(response);
            Gson gson = new Gson();
            BotData.UserList res = gson.fromJson(response, BotData.UserList.class);
            list.addAll(List.of(res.data()));
            if (page * limit >= res.count()) isAll = true;
            else page++;
        }
        return list;
    }
    public BotData.PlayerInfo getOnlinePlayerByName(String name) {
        List<BotData.PlayerInfo> list = getOnlinePlayerList();
        for (BotData.PlayerInfo info : list) {
            if (info.playername().equals(name)) return info;
        }
        return null;
    }
    public BotData.PlayerInfo getOnlinePlayerBySteamID(String steamid) {
        List<BotData.PlayerInfo> list = getOnlinePlayerList();
        for (BotData.PlayerInfo info : list) {
            if (info.platformid().equals(steamid)) return info;
        }
        return null;
    }
    public List<BotData.PlayerInfo> getOnlinePlayerList() {
        int page = 1,limit = 15;
        List<BotData.PlayerInfo> list = new ArrayList<>();
        while (true) {
            String url = rootUrl +"api/players?key="+getToken()+"&page="+page+"&limit="+limit;
            String response = Net.sendGetData(url);
            _Log.debug(response);
            Gson gson = new Gson();
            BotData.PlayerList res = gson.fromJson(response, BotData.PlayerList.class);
            list.addAll(List.of(res.data()));
            if (page * limit >= res.count()) break;
            else page++;
        }
        return list;
    }



    public boolean sendPrivateMsg(String user, String msg) {
        String url = rootUrl +"api/action_sayprivate?key="+getToken()+"&p="+user+"&name="+serverData.serverName()+"&text="+Net.urlEncode(msg);
        String response = Net.sendGetData(url);
        _Log.debug(response);
        return checkResult(response);
    }
    public boolean sendPublicMsg(String msg) {
        String url = rootUrl +"api/action_saypublic?key="+getToken()+"&name="+serverData.serverName()+"&text="+Net.urlEncode(msg);
        String response = Net.sendGetData(url);
        _Log.debug(response);
        return checkResult(response);
    }












    public boolean checkResult(String res) {
        Gson gson = new Gson();
        return checkResult(gson.fromJson(res, Web.Result.class));
    }
    public boolean checkResult(Web.Result res) {
        return res != null && res.result == 1;
    }

    public String giveReward(UserConfig user, String pack) {
        RewardData rewardData = serverCore.rewardConfig.getReward(pack);
        return switch (rewardData.type) {
            case 0: yield addPoint(user, pack, rewardData.count);
            case 1: yield giveItem(user, pack, rewardData.name, rewardData.count, rewardData.quality);
            case 2: yield dropPoint(user, pack, rewardData.count);
            default: yield "无此礼包";
        };
    }

    private String dropPoint(UserConfig user, String pack, int count) {
        if (sendPoint(user.getSteamID(),-count)) {
            user.setReward(pack);
            return "您成功领取礼包，失去"+count+"积分";
        }
        else return "领取失败";
    }

    private String giveItem(UserConfig user, String pack, String name, int count, int quality) {
        if (giveItem(user.getSteamID(), name, count, quality)) {
            user.setReward(pack);
            return "您成功领取礼包，获得"+quality+"品的【"+ OtherHelper.removeColorCodes(getItemName(name))+"】×"+count;
        }
        else return "领取失败";
    }

    private String addPoint(UserConfig user, String pack, int count) {
        if (sendPoint(user.getSteamID(),count)) {
            user.setReward(pack);
            return "您成功领取礼包，获得"+count+"积分";
        }
        else return "领取失败";
    }


    public BotData.GameState getGameState() {
        String url = rootUrl +"api/gamestats?key="+getToken();
        String response = Net.sendGetData(url);
        _Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, BotData.GameState.class);
    }
}
