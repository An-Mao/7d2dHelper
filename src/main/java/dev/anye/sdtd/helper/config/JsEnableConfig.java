package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;

import java.util.HashMap;

public class JsEnableConfig extends _JsonConfig<HashMap<String,Boolean>> {
    public JsEnableConfig(String filePath) {
        super(filePath, """
                {
                    "example":false
                }
                """, new TypeToken<>(){},false);
    }

    @Override
    public HashMap<String, Boolean> getDatas() {
        if (this.datas == null) this.datas = new HashMap<>();
        return super.getDatas();
    }

    public boolean isEnable(String key){
        return getDatas().getOrDefault(key,false);
    }
}
