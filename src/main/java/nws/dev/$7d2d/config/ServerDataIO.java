package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.core.json._JsonConfig;

public class ServerDataIO extends _JsonConfig<ServerData> {
    public ServerDataIO(String filePath) {
        super(DataTable.ServerListDir + "/" + filePath, "", new TypeToken<>(){});
    }

}
