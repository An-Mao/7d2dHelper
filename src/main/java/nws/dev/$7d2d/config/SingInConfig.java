package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.net.BotNet;
import nws.dev.$7d2d.system.DataTable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public class SingInConfig extends _JsonConfig<HashMap<String, Integer>> {
    public SingInConfig(String filePath) {
        super(DataTable.SingInDir + "/" + filePath+".json", """
                {
                    "1": 1
                }
                """, new TypeToken<>() {});
    }
    public HashMap<String, Integer> getDatas() {
        if (super.getDatas() == null) return new HashMap<>();
        return super.getDatas();
    }
    public String sign(String steamid) {
        String day = LocalDate.now().toString();
        if (getDatas().containsKey(day)) return "今日已签到,明日再来";
        Random random = new Random();
        int i = random.nextInt(Config.I.getDatas().singInMaxPoint) + 1;
        getDatas().put(day, i);
        save();
        BotNet.send_point(steamid, i);
        return "签到成功,获得" + i + "点积分";
    }
}
