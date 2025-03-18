package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.system.DataTable;

import java.util.LinkedHashMap;

public class ACItemsConfig extends _JsonConfig<LinkedHashMap<String, ACItemsData>> {
    private static final String filePath = DataTable.Dir + "/acItems.json";
    public static final ACItemsConfig I = new ACItemsConfig();
    public ACItemsConfig() {
        super(filePath, """
                {
                    "测试白名单":{
                        "allNeed":true,
                        "point":50000000,
                        "level":50000,
                        "items":["test","test1"]
                    },
                    "测试白名单1":{
                        "allNeed":false,
                        "point":50000000,
                        "level":50000,
                        "items":["test","test1"]
                    }
                }
                """, new TypeToken<>() {},false);
    }

    @Override
    public LinkedHashMap<String, ACItemsData> getDatas() {
        return super.getDatas();
    }

    public ACItemsData get(String part) {
        return getDatas().getOrDefault(part,null);
    }
}
