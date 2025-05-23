package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.DataTable;
import dev.anye.sdtd.helper.data.ServerData;

public class ServerDataIO extends _JsonConfig<ServerData> {
    public ServerDataIO(String filePath) {
        super(DataTable.ServerListDir + "/" + filePath, "", new TypeToken<>(){});
    }

}
