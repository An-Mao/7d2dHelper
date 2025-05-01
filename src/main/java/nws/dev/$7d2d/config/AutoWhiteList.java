package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.system._Log;

import java.util.HashMap;

public class AutoWhiteList extends _JsonConfig<HashMap<String,ACItemsData>> {
    private static final String filePath = DataTable.Dir + "/AutoWhiteList.json";
    public static final AutoWhiteList I = new AutoWhiteList();
    public AutoWhiteList() {
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
