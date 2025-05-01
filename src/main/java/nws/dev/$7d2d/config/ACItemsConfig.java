package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.system._Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ACItemsConfig extends _JsonConfig<LinkedHashMap<String, ACItemsData>> {
    private static final String filePath = DataTable.Dir + "/acItems.json";
    public static final ACItemsConfig I = new ACItemsConfig();
    private final List<String> allItems = new ArrayList<>();
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
        getDatas().forEach((s, acItemsData) -> allItems.addAll(List.of(acItemsData.items())));
    }

    public boolean includes(String item) {
        return allItems.contains(item);
    }


    @Override
    public LinkedHashMap<String, ACItemsData> getDatas() {
        if (this.datas == null) this.datas = new LinkedHashMap<>();
        return super.getDatas();
    }

    public ACItemsData get(String part) {
        return getDatas().getOrDefault(part,null);
    }
}
