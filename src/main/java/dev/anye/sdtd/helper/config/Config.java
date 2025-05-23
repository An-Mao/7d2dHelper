package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.DataTable;
import dev.anye.sdtd.helper.data.ConfigData;

import java.util.ArrayList;

public class Config extends _JsonConfig<ConfigData> {
    private static final String filePath = DataTable.CONFIG + "/config.json";
    public static final Config I = new Config();
    public Config() {
        super(filePath, """
                {
                    "outputDefaultConfig":true,
                    "logColor":false,
                    "isDebug":false,
                    "listenPort":11111,
                    "qqHost":"127.0.0.1:3000",
                    "admin":["123456"]
                }
                """, new TypeToken<>() {
        });
    }

    @Override
    public ConfigData getDatas() {
        if (datas == null) datas = new ConfigData(true,false,false,11111,"127.0.0.1:3000",new ArrayList<>());
        return super.getDatas();
    }
    public void setData(ConfigData configData){
        this.datas = configData;
    }

    public int listenPort(){
        return getDatas().listenPort();
    }

    public boolean isAdmin(String qq) {
        return this.getDatas().admin().contains(qq);
    }
}
