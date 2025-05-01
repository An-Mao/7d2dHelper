package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.data.ConfigData;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.net.Net;

public class Config extends _JsonConfig<ConfigData> {
    private static final String filePath = DataTable.Dir + "/config.json";
    public static final Config I = new Config();
    public Config() {
        super(filePath, """
                {
                    "logColor":false,
                    "isDebug":false,
                    "listenPort":11111,
                    "qqHost":"127.0.0.1:3000"
                }
                """, new TypeToken<>() {
        });
    }

    @Override
    public ConfigData getDatas() {
        if (datas == null) datas = new ConfigData(false,false,11111,"127.0.0.1:3000");
        return super.getDatas();
    }

    public int listenPort(){
        return getDatas().listenPort();
    }
    public String getServerName() {
        return Net.urlEncode(getDatas().serverName);
    }
}
