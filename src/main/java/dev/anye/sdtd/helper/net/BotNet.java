package dev.anye.sdtd.helper.net;

import com.google.gson.Gson;
import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.*;
import dev.anye.sdtd.helper.helper.OtherHelper;
import dev.anye.sdtd.helper.server.ServerCore;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BotNet {
    private final Gson gson = new Gson();
    private final ServerCore serverCore;
    private final ServerData serverData;

    private final String rootUrl;
    private final String loginUrl;
    private final String addPointUrl;
    private final String urlPoint;
    private final String giveItemUrl;
    private final String localUrl;

    private static Web.Bot Use = new Web.Bot();
    public BotNet(ServerCore serverCore){
        this.serverCore = serverCore;
        this.serverData = serverCore.serverData;
        this.rootUrl = "http://"+ serverData.botHost() +"/";
        this.loginUrl = rootUrl +"api/login?username="+serverData.botUsername() +"&password="+serverData.botPassword();
        this.addPointUrl = rootUrl +"api/action_pointadd?key=";
        this.giveItemUrl = rootUrl +"api/action_giveitem?key=";
        this.localUrl = rootUrl + "api/localizations?key=";
        this.urlPoint = rootUrl + "api/point?";

    }


    public boolean loginUser() {
        String response = Net.sendGetData(this.loginUrl);
        $7DTD._Log.debug("bot 登录结果："+response);
        Use = gson.fromJson(response, Web.Bot.class);
        return Use != null && Use.result == 1;
    }

    public boolean sendPoint(String user, int count) {
        String url = this.addPointUrl + getToken() +"&p="+user+"&count="+count+"&isnotice=1";
        String response = Net.sendGetData(url);
        $7DTD._Log.debug(response);
        return checkResult(response);
    }
    public boolean giveItem(String user, String name, int count, int quality) {
        String url = this.giveItemUrl+ getToken() +"&p="+user+"&name="+Net.urlEncode(name)+"&count="+count+"&quality="+quality;
        String response = Net.sendGetData(url);
        $7DTD._Log.debug(response);
        return checkResult(response);
    }
    public boolean killPlayer(String user) {
        String url = rootUrl +"api/action_kill?key="+ getToken() +"&p="+user;
        String response = Net.sendGetData(url);
        $7DTD._Log.debug(response);
        return checkResult(response);
    }



    public String getItemName(String itemId) {
        int page = 1,limit = 10;
        String name = "";
        while (name.isEmpty()) {
            String url = this.localUrl + getToken() + "&page=" + page + "&limit=" + limit + "&keyword=" + Net.urlEncode(itemId);
            String response = Net.sendGetData(url);
            $7DTD._Log.debug(response);
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
            $7DTD._Log.warn("session_key失效，尝试重新登录");
            if (!loginUser()) return "";
        }
        return Use.session_key;
    }
    public boolean checkLogin() {
        String response = Net.sendGetData(rootUrl +"api/checksession?key="+ Use.session_key);
        $7DTD._Log.debug(response);
        return checkResult(response);
    }

    public @Nullable BotData.UserInfo getUserByName(String name) {
        GetData getData = new GetData(urlPoint);
        getData.add("key",getToken());
        getData.add("page","1");
        getData.add("limit","50");
        getData.add("searchkey",name);
        String response = Net.sendGetData(getData.toString());
        BotData.UserList res = gson.fromJson(response, BotData.UserList.class);
        if (res != null && res.code()== 0){
            for (BotData.UserInfo info : res.data()) {
                if (info.playername().equals(name)) return info;
            }
        }
        return null;
    }
    public BotData.UserInfo getUserBySteamID(String sid) {
        GetData getData = new GetData(urlPoint);
        getData.add("key",getToken());
        getData.add("page",String.valueOf(1));
        getData.add("limit","50");
        getData.add("searchkey", sid);
        String response = Net.sendGetData(getData.toString());
        BotData.UserList res = gson.fromJson(response, BotData.UserList.class);
        if (res != null && res.code()== 0){
            for (BotData.UserInfo info : res.data()) {
                if (info.platformid().equals(sid)) return info;
            }
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
            $7DTD._Log.debug(response);
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
            $7DTD._Log.debug(response);
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
        $7DTD._Log.debug(response);
        return checkResult(response);
    }
    public boolean sendPublicMsg(String msg) {
        String url = rootUrl +"api/action_saypublic?key="+getToken()+"&name="+serverData.serverName()+"&text="+Net.urlEncode(msg);
        String response = Net.sendGetData(url);
        $7DTD._Log.debug(response);
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
            case 0 -> addPoint(user, pack, rewardData.count);
            case 1 -> giveItem(user, pack, rewardData.name, rewardData.count, rewardData.quality);
            case 2 -> dropPoint(user, pack, rewardData.count);
            default -> "无此礼包";
        };
    }
    public String giveReward(UserConfig user, RewardData rewardData) {
        return switch (rewardData.type) {
            case 0 -> addPoint(user, rewardData.count);
            case 1 -> giveItem(user, rewardData.name, rewardData.count, rewardData.quality);
            case 2 -> dropPoint(user, rewardData.count);
            default -> "无此礼包";
        };
    }

    private String dropPoint(UserConfig user, String pack, int count) {
        if (sendPoint(user.getSteamID(),-count)) {
            user.setReward(pack);
            return "您成功领取礼包，失去"+count+"积分";
        }
        else return "领取失败";
    }
    private String dropPoint(UserConfig user, int count) {
        if (sendPoint(user.getSteamID(),-count)) {
            return "失去"+count+"积分";
        }
        else return "操作积分失败";
    }

    private String giveItem(UserConfig user, String pack, String name, int count, int quality) {
        if (giveItem(user.getSteamID(), name, count, quality)) {
            user.setReward(pack);
            return "您成功领取礼包，获得"+quality+"品的【"+ OtherHelper.removeColorCodes(getItemName(name))+"】×"+count;
        }
        else return "领取失败";
    }
    private String giveItem(UserConfig user,String name, int count, int quality) {
        if (giveItem(user.getSteamID(), name, count, quality)) {
            return "获得"+quality+"品的【"+ OtherHelper.removeColorCodes(getItemName(name))+"】×"+count;
        }
        else return "给予物品失败";
    }

    private String addPoint(UserConfig user, String pack, int count) {
        if (sendPoint(user.getSteamID(),count)) {
            user.setReward(pack);
            return "您成功领取礼包，获得"+count+"积分";
        }
        else return "领取失败";
    }
    public String addPoint(UserConfig user,  int count) {
        if (sendPoint(user.getSteamID(),count)) {
            return "成功添加"+count+"积分";
        }
        else return "添加积分失败";
    }


    public BotData.GameState getGameState() {
        String url = rootUrl +"api/gamestats?key="+getToken();
        String response = Net.sendGetData(url);
        $7DTD._Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, BotData.GameState.class);
    }
}
