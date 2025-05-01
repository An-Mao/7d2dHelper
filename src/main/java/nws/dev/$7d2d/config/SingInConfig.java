package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.net.BotNet;
import nws.dev.$7d2d.system._Log;

import java.time.LocalDate;
import java.util.HashMap;

public class SingInConfig extends _JsonConfig<HashMap<String, Long>> {
    public SingInConfig(String filePath) {
        super(DataTable.SingInDir + "/" + filePath+".json", """
                {
                    "1": 1
                }
                """, new TypeToken<>() {});
    }
    public HashMap<String, Long> getDatas() {
        if (super.getDatas() == null) return new HashMap<>();
        return super.getDatas();
    }
    public String sign(String steamid){
        return sign(steamid,"");
    }

    public String sign(String steamid,String other){
        String day = LocalDate.now().toString();
        if (getDatas().containsKey(day)) return "今日已签到,明日再来";
        if (BotNet.getOnlinePlayerBySteamID(steamid) == null) return "玩家不在线，无法获取签到奖励";

        StringBuilder s = new StringBuilder("今日签到奖励:");

        SingInPlusConfig selfBindConfig = SingInPlusConfig.get(steamid);
        //String yday = LocalDate.now().minusDays(1).toString();

        //如果上次绑定对象没有当天签到，则无任何奖励
        if (selfBindConfig.getDatas().steamid().equals("-1")) giveReward(steamid,s);
        else {
            selfBindConfig.setData("-1", "-1");
            selfBindConfig.save();
            s.append("\\n").append("无");
        }
        //保存签到数据
        getDatas().put(day, System.currentTimeMillis());
        save();

        //给予绑定者额外奖励，并重置绑定对象
        SingInPlusConfig o = SingInPlusConfig.geto(steamid);
        if (!o.getDatas().steamid().equals("-1") && o.getDatas().time().equals(day)){
            _Log.debug("模拟奖励 1");

            if (BotNet.getOnlinePlayerBySteamID(o.getDatas().steamid()) == null){
                s.append("\\n对方不在线，无法获取额外签到奖励");
            }else {
                s.append("\\n").append("对方额外奖励：");
                giveReward(o.getDatas().steamid(), s);
            }


            //对方重置绑定对象
            if (!o.getDatas().steamid().equals("-1")) {
                SingInPlusConfig ox = SingInPlusConfig.get(o.getDatas().steamid());
                ox.setData("-1", "0");
                ox.save();
                //重置绑定自己对象
                o.setData("-1", "0");
                o.save();
            }
        }

        //绑定
        if (!other.isEmpty()){
            if (other.equals(steamid)) s.append("\\n无法绑定自己");
            else {
                o = SingInPlusConfig.geto(other);
                if (o.getDatas().steamid().equals("-1")) {
                    o.setData(steamid, day);
                    o.save();
                    selfBindConfig.setData(other, day);
                    selfBindConfig.save();
                    s.append("\\n签到绑定成功");
                } else {
                    selfBindConfig.setData("-1", "0");
                    selfBindConfig.save();
                    s.append("\\n对方已被其他人绑定");
                }
            }
        }



        /*


        Random random = new Random();
        int i = random.nextInt(Config.I.getDatas().singInMaxPoint) + 1;
        BotNet.send_point(steamid, i);

         */
        return s.toString();
    }
    public void giveReward(String steamid,StringBuilder s){

        DailyRewardsConfig.I.getDatas().forEach(dailyRewards -> {
            if (dailyRewards.getType() == DailyRewardsConfig.RewardType.POINT) {
                int point = dailyRewards.getCount();
                if (point > 0) BotNet.sendPoint(steamid, point);
                s.append("\\n").append(point).append(dailyRewards.name() == null ? "积分" : dailyRewards.name());
            }else if (dailyRewards.getType() == DailyRewardsConfig.RewardType.ITEM) {
                int count = dailyRewards.getCount();
                if (count > 0) BotNet.giveItem(steamid, dailyRewards.id(),count, dailyRewards.quality());
                s.append("\\n").append(count).append(dailyRewards.name());
            }
        });
    }
}
