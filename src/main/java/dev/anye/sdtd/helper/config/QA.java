package dev.anye.sdtd.helper.config;

import com.google.gson.reflect.TypeToken;
import dev.anye.core.json._JsonConfig;

import java.util.HashMap;

public class QA extends _JsonConfig<HashMap<String, String>> {
    public QA(String filePath) {
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
