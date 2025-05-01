package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.data.PlayerInfoData;
import nws.dev.$7d2d.data.UserData;
import nws.dev.$7d2d.json._JsonConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserConfig extends _JsonConfig<UserData> {
    public UserConfig(String qq) {
        super(DataTable.UserDir + "/" + qq + ".json", """
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

    public String getSteamID() {
        return getDatas().platformid;
    }


    public int getRecordItemLimit() {
        return getDatas().recordItemLimit;
    }

    public List<PlayerInfoData.ItemData> getRecordItemMap() {
        return getRecordItem().getDatas();
    }
    public RecordItem getRecordItem() {
        return new RecordItem(getSteamID());
    }

    public static class RecordItem extends _JsonConfig<List<PlayerInfoData.ItemData>>{

        public RecordItem(String steamID) {
            super(DataTable.UserItemDir + "/" + steamID + ".json", "", new TypeToken<>() {},false);
        }
        public List<PlayerInfoData.ItemData> getDatas() {
            if (this.datas == null) this.datas = new ArrayList<>();
            return this.datas;
        }
    }
}
