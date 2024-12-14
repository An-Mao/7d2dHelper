package nws.dev.$7d2d.data;

public class KitData {
    public record Command(String command, String parameters, String result) { }
    public record GameMsg(int result, Msg[] list){}
    /**
     * 玩家消息
     * @param id 行
     * @param pn 玩家名
     * @param msg 消息
     * @param tp 聊天消息类型，0为公共频道，1为好友频道，2为组队频道，3为私聊频道
     * @param p 玩家SteamID
     * @param t 时间
     */
    public record Msg(int id,String pn,String msg,int tp,String p,int t){}
    public record PlayerInfo(String steamid,String name){ }
    public record GsList(int result,GsInfo[] list){}
    public record GsInfo(String uuid,int status,int players,int time,int timetype,int fps){}
}
