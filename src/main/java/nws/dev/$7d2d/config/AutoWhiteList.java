package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.data.ACItemsData;
import nws.dev.$7d2d.json._JsonConfig;

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
