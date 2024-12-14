package nws.dev.$7d2d.command;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.helper.ServerHelper;
import nws.dev.$7d2d.net.BotNet;
import nws.dev.$7d2d.net.KitNet;
import nws.dev.$7d2d.net.QQNet;
import nws.dev.$7d2d.system._Log;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ServerCommand {
    public static void restart(){
        QQNet.restartServer();
    }
    public static void reloadConfig(){
        _Log.info("即将重新加载配置");
        Config.I.init();
        _Log.info("重新加载配置完成");
    }


    public static void bind(KitData.Msg msg){
        if (QQNet.bindUser.containsKey(msg.p())){
            String qq = QQNet.bindUser.get(msg.p());
            UserConfig config = new UserConfig(qq);
            config.setBindDone(msg.p());
            _Log.info("玩家 "+msg.pn()+" 绑定成功,QQ号为 "+qq);
            BotNet.sendPrivateMsg(msg.p(), "绑定成功,QQ号为 "+qq);
        }

    }




    public static boolean isVote = false;
    public static long voteStartTime = 0;
    public static List<String> voteList = new ArrayList<>();
    private static final Thread voteHeart = new Thread(() -> {
        int i = Config.I.getDatas().voteTime;
        while (i > 0 && isVote) {
            i--;
            try {
                voteCheck();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        voteList.clear();
        _Log.info("投票结束");
        ServerHelper.sendChatMsg("投票结束,投票功能将在"+Config.I.getDatas().voteCooldown+"秒内不可用");
        isVote = false;
    });
    public static void voteCheck(){
        int a = 0;
        KitData.PlayerInfo[] list = ServerHelper.getPlayerList();
        for (KitData.PlayerInfo info : list) {
            if (voteList.contains(info.steamid()))a++;
        }
        if (a >= Math.round(list.length * Config.I.getDatas().voteScale)) {
            isVote = false;
            voteList.clear();
            _Log.info("投票结束");
            ServerHelper.sendChatMsg("投票结束即将清理服务器!!!");
            restart();
        }
    }
    public static void voteClear(KitData.Msg msg) {

        if (!isVote && System.currentTimeMillis() - voteStartTime < Config.I.getDatas().voteCooldown * 1000L) {
            _Log.info("投票冷却中");
            BotNet.sendPrivateMsg(msg.p(), "投票冷却中,请耐心等待");
            return;
        }
        if (voteList.contains(msg.p())) {
            _Log.info("玩家【" + msg.pn() + "】已经投过票");
            return;
        }
        List<String> playerMsg = new ArrayList<>();
        if (voteHeart.isAlive()) {
            playerMsg.add("玩家【" + msg.pn() + "】参与了投票");
            playerMsg.add("当前已投票人数:" + voteList.size());
            playerMsg.add("如果您也想清理服务器,请发送【清理】");
        } else {
            int playerNumber = ServerHelper.getPlayerList().length;
            playerMsg.add("玩家【" + msg.pn() + "】发起了清理投票");
            playerMsg.add("服务器总人数:" + playerNumber);
            playerMsg.add("清理至少需要" + Math.round(playerNumber * Config.I.getDatas().voteScale) + "个人参与.");
            playerMsg.add("如果您也想清理服务器,请发送【清理】");
            isVote = true;
            voteStartTime = System.currentTimeMillis();
            voteHeart.start();
        }
        voteList.add(msg.p());
        _Log.info(playerMsg.toArray(String[]::new));
        ServerHelper.sendChatMsg(playerMsg.toArray(String[]::new));
        voteCheck();

    }


    public static String getServerInfo(){
        KitData.GsList list = KitNet.getGsList();
        if (list.result() == 1) {
            StringBuilder s = new StringBuilder("===服务器信息===");
            for (KitData.GsInfo info : list.list()) {
                s.append("\\n运行状态：").append(getServerStatus(info.status()));
                s.append("\\n当前帧率：").append(info.fps());
                s.append("\\n在线人数：").append(info.players());
                s.append("\\n重启时间：").append(LocalTime.ofSecondOfDay(info.time()));
            }
            s.append("\\n").append(BotNet.getGameState().toString());
            return s.toString();
        }
        return "获取服务器信息失败";
    }
    public static String getServerStatus(int status){
        return switch (status){
            case 1 -> "运行中";
            case 2 -> "重启中";
            case 3 -> "已关闭";
            default -> "未知";
        };
    }

    public static void help(KitData.Msg m) {
        BotNet.sendPrivateMsg(m.p(),"====服务器指令====");
        BotNet.sendPrivateMsg(m.p(),"【清理】发起投票清理");
        BotNet.sendPrivateMsg(m.p(),"【绑定账号】绑定QQ账号,需要先在QQ内发送");
    }
}
