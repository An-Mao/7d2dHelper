package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.data.PlayerInfoData;
import dev.anye.sdtd.helper.data.UserData;
import dev.anye.sdtd.helper.server.ServerCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserConfig extends _JsonConfig<UserData> {
    public UserConfig(String qq) {
        super(qq, """
                {
                    "userid":"",
                    "platformid":"",
                    "reward":{
                        "NewbiePack":false
                    },
                    "recordItemLimit":0,
                    "canExtractSaveItem":false
                }
                """, new TypeToken<>() {
        });
    }

    @Override
    public UserData getDatas() {
        if (super.getDatas() == null) return new UserData();
        return super.getDatas();
    }
    public HashMap<String,Boolean> getReward() {
        if (getDatas().reward == null) getDatas().reward = new HashMap<>();
        return getDatas().reward;
    }
    public boolean isBind(){
        return !getDatas().userid.isEmpty() && !getDatas().platformid.isEmpty();
    }
    public void setBind(String userid){
        getDatas().userid = userid;
        save();
    }
    public void setBindDone(String steamid){
        getDatas().platformid = steamid;
        save();
    }
    public boolean isReward(String reward) {
        return getReward().getOrDefault(reward, false);
    }

    public void setReward(String reward) {
        getReward().put(reward, true);
        save();
    }
    public String getUserId(){
        return getDatas().userid;
    }

    public String getSteamID() {
        return getDatas().platformid;
    }


    public int getRecordItemLimit() {
        return getDatas().recordItemLimit;
    }

    public List<PlayerInfoData.ItemData> getRecordItemMap(ServerCore serverCore) {
        return getRecordItem(serverCore).getDatas();
    }
    public RecordItem getRecordItem(ServerCore serverCore) {
        return new RecordItem(serverCore,getSteamID());
    }

    public static class RecordItem extends _JsonConfig<List<PlayerInfoData.ItemData>>{

        public RecordItem(ServerCore serverCore,String steamID) {
            super(serverCore.userItemDir + "/" + steamID + ".json", "", new TypeToken<>() {},false);
        }
        public List<PlayerInfoData.ItemData> getDatas() {
            if (this.datas == null) this.datas = new ArrayList<>();
            return this.datas;
        }
    }
}
