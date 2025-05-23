package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;

import java.util.ArrayList;
import java.util.List;

public class DailyRewardsConfig extends _JsonConfig<List<DailyRewardsConfig.DailyRewards>> {
    public DailyRewardsConfig(String filePath) {
        super(filePath, """
                [
                    {
                        "type": 0,
                        "min": 0,
                        "max": 10000
                    },
                    {
                        "type": 1,
                        "id": "物品id",
                        "name": "显示的名称",
                        "quality": 0,
                        "min": 0,
                        "max": 0
                    }
                ]
                """, new TypeToken<>(){},false);
    }

    @Override
    public List<DailyRewards> getDatas() {
        if (datas == null) return new ArrayList<>();
        return datas;
    }

    public record DailyRewards(int type, String id, int min, int max, int quality, String name) {
        public RewardType getType() {
            return RewardType.values()[type];
        }
        public DailyRewards(int type, int min, int max) {
            this(type, "", min, max, 0, "积分");
        }
        public int getCount() {
            return (int) Math.floor(Math.random() * (max - min + 1) + min);
        }
    }
    public enum RewardType {
        POINT,
        ITEM
    }
}
