package nws.dev.$7d2d.config;

import com.google.gson.reflect.TypeToken;
import nws.dev.$7d2d.json._JsonConfig;
import nws.dev.$7d2d.system.DataTable;

import java.time.LocalTime;
import java.util.*;

public class EventListConfig extends _JsonConfig<HashMap<String, String>> {
    private static final String filePath = DataTable.Dir + "/eventList.json";
    public static EventListConfig I = new EventListConfig();
    private boolean n;
    public EventListConfig() {
        super(filePath, """
                {
                    "09:00": "迷宫",
                    "10:00": "世界BOSS",
                    "11:00": "坤坤大作战",
                    "13:00": "世界BOSS",
                    "15:00": "迷宫",
                    "16:00": "世界BOSS",
                    "19:00": "迷宫",
                    "19:30": "载具竞速",
                    "20:00": "世界BOSS",
                    "20:30": "载具竞速",
                    "21:00": "坤坤大作战",
                    "21:30": "载具竞速"
                }
                """, new TypeToken<>() {},false);
    }

    @Override
    public HashMap<String, String> getDatas() {
        HashMap<String, String> map = super.getDatas();
        if (map == null) map =  new HashMap<>();
        if (n) return map;
        List<Map.Entry<String, String>> entryList = new ArrayList<>(map.entrySet());

        entryList.sort((entry1, entry2) -> {
            LocalTime time1 = LocalTime.parse(entry1.getKey());
            LocalTime time2 = LocalTime.parse(entry2.getKey());
            return time1.compareTo(time2);
        });
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        this.datas = sortedMap;
        return sortedMap;
    }
}
