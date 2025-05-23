package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.data.ACItemsData;

import java.util.HashMap;

public class AutoWhiteList extends _JsonConfig<HashMap<String, ACItemsData>> {
    public AutoWhiteList(String filePath) {
        super(filePath, """
                {
                    "testA":{
                        "allNeed":false,
                        "point":50000000,
                        "level":50000,
                        "items":["test","test1"]
                    }
                }
                """, new TypeToken<>(){},false);
    }

    @Override
    public HashMap<String, ACItemsData> getDatas() {
        if (this.datas == null) this.datas = new HashMap<>();
        return super.getDatas();
    }

    public void clear() {
        getDatas().clear();
    }
    public void push(String key,ACItemsData data){
        getDatas().put(key,data);
    }
    public void saveAll(){
        super.save();
    }

}
