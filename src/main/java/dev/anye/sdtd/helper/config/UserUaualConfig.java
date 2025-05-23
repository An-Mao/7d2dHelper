package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.DataTable;
import dev.anye.sdtd.helper.data.UserUsualData;

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
