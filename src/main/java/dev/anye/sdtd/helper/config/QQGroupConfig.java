package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;
import dev.anye.sdtd.helper.DataTable;

import java.util.HashMap;

public class QQGroupConfig extends _JsonConfig<HashMap<String,String>> {
    private static final String file = DataTable.CONFIG +"/qqGroupServer.json";
    public static final QQGroupConfig I = new QQGroupConfig(file);
    public QQGroupConfig(String filePath) {
        super(filePath, """
                {
                    "123456":"example"
                }
                """, new TypeToken<>(){},false);
    }
}
