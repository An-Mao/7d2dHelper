package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.core.json._JsonConfig;

import java.util.HashMap;

public class EventsConfig extends _JsonConfig<HashMap<String, String>> {
    public EventsConfig(String file) {
        super(file,"""
                {
                    "08:30": "迷宫即将在30分钟后开启",
                    "08:50": "迷宫即将在10分钟后开启",
                    "08:55": "迷宫即将在5分钟后开启",
                    "09:00": "迷宫已开启",
                    "09:30": "世界BOSS即将在30分钟后生成",
                    "09:50": "世界BOSS即将在10分钟后生成",
                    "09:55": "世界BOSS即将在5分钟后生成",
                    "10:00": "世界BOSS已生成",
                    "10:30": "坤坤大作战即将在30分钟后开启",
                    "10:50": "坤坤大作战即将在10分钟后开启",
                    "10:55": "坤坤大作战即将在5分钟后开启",
                    "11:00": "坤坤大作战已开启",
                    "12:30": "世界BOSS即将在30分钟后生成",
                    "12:50": "世界BOSS即将在10分钟后生成",
                    "12:55": "世界BOSS即将在5分钟后生成",
                    "13:00": "世界BOSS已生成",
                    "14:30": "迷宫即将在30分钟后开启",
                    "14:50": "迷宫即将在10分钟后开启",
                    "14:55": "迷宫即将在5分钟后开启",
                    "15:00": "迷宫已开启",
                    "15:30": "世界BOSS即将在30分钟后生成",
                    "15:50": "世界BOSS即将在10分钟后生成",
                    "15:55": "世界BOSS即将在5分钟后生成",
                    "16:00": "世界BOSS已生成",
                    "18:30": "迷宫即将在30分钟后开启",
                    "18:50": "迷宫即将在10分钟后开启",
                    "18:55": "迷宫即将在5分钟后开启",
                    "19:00": "迷宫已开启",
                    "19:20": "载具竞速即将在10分钟后开启",
                    "19:30": "载具竞速已开启",
                    "19:50": "世界BOSS即将在10分钟后生成",
                    "19:55": "世界BOSS即将在5分钟后生成",
                    "20:00": "世界BOSS已生成",
                    "20:20": "载具竞速即将在10分钟后开启",
                    "20:30": "载具竞速已开启",
                    "20:50": "坤坤大作战即将在10分钟后开启",
                    "20:55": "坤坤大作战即将在5分钟后开启",
                    "21:00": "坤坤大作战已开启",
                    "21:20": "载具竞速即将在10分钟后开启",
                    "21:30": "载具竞速已开启"
                }
                """ , new TypeToken<>() {},false);
    }

    @Override
    public HashMap<String, String> getDatas() {
        if (super.getDatas() == null) return new HashMap<>();
        return super.getDatas();
    }

    public String get(String time) {
        return getDatas().getOrDefault(time,"");
    }
}
