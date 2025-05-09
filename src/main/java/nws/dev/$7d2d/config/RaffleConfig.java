package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.data.RaffleData;
import nws.dev.$7d2d.json._JsonConfig;

import java.util.ArrayList;

public class RaffleConfig extends _JsonConfig<RaffleData> {
    public RaffleConfig(String filePath) {
        super(filePath, """
                {
                    "enabled": true,
                    "point":100000,
                    "cooldown":600,
                    "items":[
                        {
                            "reward":{
                                "type": 1,
                                "name": "terrDirt",
                                "quality": 0,
                                "count": 1
                            },
                            "weight":100
                        }
                    ]
                }
                """, new TypeToken<>() {},false);
    }

    @Override
    public RaffleData getDatas() {
        if (datas == null) datas = new RaffleData(false,100000,600,new ArrayList<>());
        return super.getDatas();
    }
}
