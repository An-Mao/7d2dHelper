package nws.dev.$7d2d.data;

import nws.dev.$7d2d.net.Net;

public record ACItemsData(String[] items, int point, int level, boolean allNeed) {

    public String getFormatItems() {
        StringBuilder result = new StringBuilder();
        for (String item : items) {
            if (item != null){
                if (result.isEmpty()) result.append(Net.urlEncode(item));
                else result.append("%09").append(Net.urlEncode(item));
            }
        }
        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("白名单信息");
        result.append("\\n需要分数：").append(point);
        result.append("\\n需要等级：").append(level);
        result.append("\\n需要全部满足：").append(allNeed?"是":"否");
        result.append("\\n包含物品：");
        for (String item : items) result.append("\\n  ").append(item);
        return result.toString();
    }
}
