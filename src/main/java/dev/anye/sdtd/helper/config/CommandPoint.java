package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;

import java.util.HashMap;

public class CommandPoint extends _JsonConfig<HashMap<String,Integer>> {

    public CommandPoint(String filePath) {
        super(filePath, """
                {
                    "查详细信息":1000
                }
                """, new TypeToken<>(){});
    }

    @Override
    public HashMap<String, Integer> getDatas() {
        if (this.datas == null) this.datas = new HashMap<>();
        return super.getDatas();
    }

    public int getPoint(String c) {
        return getDatas().getOrDefault(c,100);
    }
}
