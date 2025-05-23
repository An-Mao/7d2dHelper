package dev.anye.sdtd.helper.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public record BanInfo(int result,int total,BanUser[] users){}
    public record BanUser(String steamid,String name,String pos,long banuntil,long createtime,long logintime,String banreason,String ip){}

    public record Players(int result, int total, List<Player> users){

    }

    public record Player(long createtime,int health,String ip,int level,long logintime,String name,String pos,int priv,int skillpoint,int status,String steamid){
        public String getCreateTime(){
            return toTime(createtime* 1000L);
        }
        public String getLoginTime(){
            return toTime(logintime* 1000L);
        }

        public static String toTime(long timestamp) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
        public String getStatus(){
            return switch (status) {
                case 0 -> PlayerState.Online.toString();
                case 1 -> PlayerState.OnJoin.toString();
                case 2 -> PlayerState.OnWait.toString();
                case 3 -> PlayerState.Ban.toString();
                case 4 -> PlayerState.Offline.toString();
                default -> "未知";
            };
        }
    }
    public enum PlayerState{
        Online(0,"在线"),
        OnJoin(1,"正在进入"),
        OnWait(2,"排队中"),
        Ban(3,"已封禁"),
        Offline(4,"离线");
        final int state;
        final String desc;
        PlayerState(int state,String desc){
            this.state = state;
            this.desc = desc;
        }
        public static PlayerState fromInt(int state) {
            for (PlayerState t : PlayerState.values()) {
                if (t.state == state) {
                    return t;
                }
            }
            throw new IllegalArgumentException("No enum constant with value " + state);
        }

        @Override
        public String toString() {
            return desc;
        }
    }
}
