package dev.anye.sdtd.helper.data;

import com.google.gson.Gson;

import java.util.LinkedHashMap;

public class PostData {
    private final LinkedHashMap<String, String> data = new LinkedHashMap<>();
    public PostData() {}
    public void add(String key, String value) { data.put(key, value); }
    public String toJson() { return new Gson().toJson(data); }

    @Override
    public String toString() {
        return "PostData{" +
                "data=" + data +
                '}';
    }
}
