package dev.anye.sdtd.helper.data;

import java.util.LinkedHashMap;

public class GetData {
    private final String url;
    private final LinkedHashMap<String, String> data = new LinkedHashMap<>();

    public GetData() {
        this("");
    }
    public GetData(String url) {
        this.url = url;
    }
    public void add(String key, String value) { data.put(key, value); }
    public String format() {
        StringBuilder stringBuilder = new StringBuilder();
        data.forEach((s, o) -> {
            if (s != null && o != null) {
                if (stringBuilder.isEmpty())stringBuilder.append(s).append("=").append(o);
                else stringBuilder.append("&").append(s).append("=").append(o);
            }
        });
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return url + format();
    }
}
