package nws.dev.$7d2d.config;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.server.ServerList;
import nws.dev.core.system._File;

import java.util.HashMap;

public class Configs {
    public static final HashMap<String,String> groupServer = new HashMap<>();


    public static void init(){
        LoadServerData();
        LoadGroupServer();
    }

    private static void LoadGroupServer() {
        groupServer.clear();
        if (QQGroupConfig.I.getDatas() != null) groupServer.putAll(QQGroupConfig.I.getDatas());
    }

    public static void LoadServerData() {
        ServerList.LIST.clear();
        _File.getFiles(DataTable.ServerListDir,".json").forEach(path -> {
            $7DTD._Log.debug("file:"+path.getFileName().toString());
            ServerData serverDataIO = new ServerDataIO(path.getFileName().toString()).getDatas();
            if (serverDataIO != null) ServerList.LIST.put(getFileNameWithoutExtension(path.getFileName().toString()),new ServerCore(serverDataIO));
        });
    }


    public static String getFileNameWithoutExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(0, dotIndex);
        } else {
            return filename;
        }
    }
}
