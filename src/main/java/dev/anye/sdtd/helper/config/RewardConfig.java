package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.data.RewardData;

import java.util.HashMap;

public class RewardConfig extends _JsonConfig<HashMap<String, RewardData>> {
    public RewardConfig(String filePath) {
        super(filePath, """
                {
                    "NewbiePack": {
                        "type": 0,
                        "count": 10000
                    },
                    "TestPack": {
                        "type": 2,
                        "count": 1
                    },
                    "TestPack1": {
                        "type": 1,
                        "name": "terrDirt",
                        "quality": 0,
                        "count": 1
                    }
                }
                """, new TypeToken<>() {
        });
    }
    public RewardData getReward(String reward) {
        return getDatas().get(reward);
    }
}
