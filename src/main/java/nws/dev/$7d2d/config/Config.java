package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.data.ConfigData;
import nws.dev.$7d2d.json._JsonConfig;

import java.util.ArrayList;

public class Config extends _JsonConfig<ConfigData> {
    private static final String filePath = DataTable.Dir + "/config.json";
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
