package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.DataTable;

import java.util.HashMap;

public class JsCommands extends _JsonConfig<HashMap<String,String>> {
    public static final JsCommands I = new JsCommands(DataTable.CONFIG+"/JsCommands.json");


    public JsCommands(String filePath) {
        super(filePath, """
                {
                    "example":"测试"
                }
                """, new TypeToken<>(){},false);
    }

    @Override
    public HashMap<String, String> getDatas() {
        if (this.datas == null) this.datas = new HashMap<>();
        return super.getDatas();
    }

    public String getCommand(String key){
        return getDatas().getOrDefault(key,"");
    }
}
