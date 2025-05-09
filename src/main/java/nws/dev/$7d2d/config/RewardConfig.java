package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.data.RewardData;
import nws.dev.$7d2d.json._JsonConfig;

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
