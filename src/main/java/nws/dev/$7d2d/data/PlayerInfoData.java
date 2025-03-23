package nws.dev.$7d2d.data;

import java.util.HashMap;
import java.util.List;

public record PlayerInfoData(
        int result,String steamid,String name,String entityid,
        String stamina,String killed,String health,
        String skillpoint,String killedPlayers,String killedZombies,String experience,
        String level,String pos,String createtime,String logintime,String ip,
        List<ItemData> inventory,List<ItemData> bag,List<ItemData> equip,
        List<LogData> log
) {

    public HashMap<String,Integer> getBagItems(){
        HashMap<String,Integer> map = new HashMap<>();
        bag.forEach(b -> map.put(b.n(), b.c()));
        return map;
    }




    /**
     * 物品基础数据
     * @param id 物品索引
     * @param c 物品数量
     * @param n 物品名称
     * @param s
     * @param q 品质
     * @param m
     * @param i 物品id
     * @param tc 物品颜色？
     */
    public record BaseItemData(int id,int c,String n,int s ,int q,int m,String i,String tc){}
    /**
     * 物品数据
     * @param id 物品索引（位置）
     * @param c 物品数量
     * @param n 物品名称
     * @param s
     * @param q 品质
     * @param m 附加数据
     * @param i 物品id
     * @param tc 物品颜色？
     */
    public record ItemData(int id, int c, String n, int s , int q, int m, String i, String tc, List<BaseItemData> mod,List<BaseItemData> cmod){}

    public record LogData(int evt,int tm){}
}
