package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.json._JsonConfig;

import java.util.HashMap;

public class QA extends _JsonConfig<HashMap<String, String>> {
    private static final String filePath = DataTable.Dir + "/QA.json";
    public static final QA I = new QA();
    public QA() {
        super(filePath, """
                {
                    "问": "答"
                }
                """, new TypeToken<>(){},false);
    }

    @Override
    public HashMap<String, String> getDatas() {
        if (datas == null) return new HashMap<>();
        return datas;
    }

    public String getAnswer(String question) {
        return getDatas().getOrDefault(question,"");
    }
}
