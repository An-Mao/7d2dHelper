package nws.dev.$7d2d.data;

public class BotData {
    public record UserList(int code,String msg,int count,UserInfo[] data) {}

    /**
     * Bot 玩家信息
     * @param userid 用户ID
     * @param platformid 平台ID
     * @param playername 玩家名
     * @param point 积分
     * @param extraslots 已购买货架格子数
     * @param sbanned
     * @param backpos
     * @param detail
     */
    public record UserInfo(String userid, String platformid,String playername,int point,int extraslots,int sbanned,String backpos,int detail) {
    }


    public record PlayerInfo(int entityid,String playername,String userid,String platformid,int level,int health,int skillpoint,String pos,String ip,int killzombie,int killplayer,int killed,int priv,int ping,int point) {
        @Override
        public String toString() {
            return "玩家名称："+playername
                    + "\\n等级："+level
                    + "\\n血量："+health
                    + "\\n死亡次数："+killed
                    + "\\n丧尸击杀数："+killzombie
                    + "\\n延迟："+ping;
        }
    }
    public record PlayerList(int code,String msg,int count,PlayerInfo[] data) {}

    public record ItemInfo(String itemid, String itemname) {}
    public record ItemList(int code,String msg,int count,ItemInfo[] data) {}
    public record GameState(int result,String day,String hour,String min,int players,int zombies,int animals) {
        @Override
        public String toString() {
            if (result == 1) return "游戏时间：第"+day+"天 "+hour+"小时"+min+"分钟\\n当前僵尸数："+zombies+"\\n当前动物数："+animals;
            return "";
        }
    }

    public record GameMsgHistory(int code,String msg,GameMsg[] data) {}
    public record GameMsg(String date,int ia,int id,String msg,String pn,String uid) {}
    public record MsgHistory(int result,int count,Msg[] data) {}
    public record Msg(String date,int ia,int id,String msg,String pn,String uid,int type) {}
}
