package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.core.json._JsonConfig;

import java.util.HashMap;

public class CommandConfig extends _JsonConfig<HashMap<String, Boolean>> {
    public CommandConfig(String filePath) {
        super(filePath, """
                {
                    "帮助": true
                }
                """, new TypeToken<>(){},false);
    }

    @Override
    public HashMap<String, Boolean> getDatas() {
        if (this.datas == null) this.datas = new HashMap<>();
        return super.getDatas();
    }

    public boolean isEnable(String command){
        return !this.getDatas().getOrDefault(command,false);
    }
}
