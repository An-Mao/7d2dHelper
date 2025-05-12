package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.core.json._JsonConfig;

import java.util.HashMap;

public class QQGroupConfig extends _JsonConfig<HashMap<String,String>> {
    private static final String file = DataTable.Dir +"/qqGroupServer.json";
    public static final QQGroupConfig I = new QQGroupConfig(file);
    public QQGroupConfig(String filePath) {
        super(filePath, """
                {
                    "123456":"example"
                }
                """, new TypeToken<>(){},false);
    }
}
