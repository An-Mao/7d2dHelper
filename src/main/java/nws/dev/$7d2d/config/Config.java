package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.net.Net;
import nws.dev.$7d2d.system.DataTable;

public class Config extends _JsonConfig<ConfigData> {
    private static final String filePath = DataTable.Dir + "/config.json";
    public static final Config I = new Config();
    public Config() {
        super(filePath, """
                {
                    "logColor":false,
                    "isDebug":false,
                    "qqGroup":["123456","431933201"],
                    "serverName":"Server",
                    "kitExePath":"D:\\\\somdir\\\\kit.exe",
                    "singInMaxPoint":10000,
                    "adminToken": "123456",
                    "kitHost": "127.0.0.1:26901",
                    "kitUsername":"admin",
                    "kitPassword":"123456",
                    "botHost": "127.0.0.1:26906",
                    "botUsername":"admin",
                    "botPassword":"123456",
                    "acHost": "127.0.0.1:26906",
                    "acUsername":"admin",
                    "acPassword":"123456",
                    "clearSetTime":600,
                    "actualRestartTime":7200,
                    "waitTime":60,
                    "listenPort":11111,
                    "heartInterval":200,
                    "msgCount":100,
                    "restartMode":1,
                    "restartTime":120,
                    "voteTime":60,
                    "voteScale":0.5,
                    "voteCooldown":300,
                    "qqHost":"127.0.0.1:3000",
                    "adminQQ":["123456"],
                    "unBanNum":5,
                    "bindNeedGameMsg":false,
                    "qqMsgType":0,
                    "enableKitHeartbeat":false
                }
                """, new TypeToken<>() {
        });
    }
    public String getServerName() {
        return Net.urlEncode(getDatas().serverName);
    }
}
