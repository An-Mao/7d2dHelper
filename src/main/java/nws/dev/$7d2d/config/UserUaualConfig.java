package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.core.json._JsonConfig;

public class UserUaualConfig extends _JsonConfig<UserUsualData> {
    public UserUaualConfig(String filePath) {
        super(DataTable.UserDataDir +"/"+ filePath+".json", """
                {
                    "privateServer":""
                }
                """, new TypeToken<>(){});
    }
    public String privateServer(){
        return getDatas().privateServer();
    }
}
