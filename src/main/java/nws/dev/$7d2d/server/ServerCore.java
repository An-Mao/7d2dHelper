package nws.dev.$7d2d.server;

import com.google.gson.Gson;
import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.data.ServerData;
import nws.dev.$7d2d.net.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerCore extends ServerConfig{
    
    public final BotNet botNet;
    public final KitNet kitNet;
    public final ACNet acNet;
    public final Heartbeat heartbeat;
    public final Thread restartThread ;
    private final Thread voteHeart;
    private final Thread tt ;
    public ServerCore(ServerData serverData){
        super(serverData);
        $7DTD._Log.info("初始化Bot");
        botNet = new BotNet(this);
        $7DTD._Log.info("初始化Kit");
        kitNet = new KitNet(this.serverData);
        $7DTD._Log.info("初始化AC");
        acNet = new ACNet(this.serverData);
        $7DTD._Log.info("初始化心跳");
        heartbeat = new Heartbeat(this);

        restartThread = new Thread(() -> {
            int time = serverData.restartTime();
            botNet.sendPublicMsg("即将重启服务器，请您做好准备以防数据丢失。");
            while (time > 0) {
                time--;
                switch (time) {
                    case 0: botNet.sendPublicMsg("重启服务器中。。。");
                    case 1: botNet.sendPublicMsg("1 秒后开始重启，建议原地等待。");
                    case 2: botNet.sendPublicMsg("2 秒后开始重启，建议原地等待。");
                    case 3: botNet.sendPublicMsg("3 秒后开始重启，建议原地等待。");
                    case 4: botNet.sendPublicMsg("4 秒后开始重启，建议原地等待。");
                    case 5: botNet.sendPublicMsg("5 秒后开始重启，建议原地等待。");
                    case 10: botNet.sendPublicMsg("10 秒后开始重启，建议原地等待。");
                    case 15: botNet.sendPublicMsg("15 秒后开始重启，建议原地等待。");
                    case 20: botNet.sendPublicMsg("20 秒后开始重启，建议原地等待。");
                    case 25: botNet.sendPublicMsg("25 秒后开始重启，建议原地等待。");
                    case 30: botNet.sendPublicMsg("30 秒后开始重启，建议原地等待。");
                    case 35: botNet.sendPublicMsg("35 秒后开始重启，建议原地等待。");
                    case 40: botNet.sendPublicMsg("40 秒后开始重启，建议原地等待。");
                    case 45: botNet.sendPublicMsg("45 秒后开始重启，建议原地等待。");
                    case 60: botNet.sendPublicMsg("60 秒后开始重启，建议原地等待。");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            $7DTD._Log.warn("开始重启");
            kitNet.stopNet();
            runKitExe();
            kitNet.startServer();
        });
        voteHeart  = new Thread(() -> {
            int i = serverData.voteTime();
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
            $7DTD._Log.info("投票结束");
            sendChatMsg("投票结束,投票功能将在"+serverData.voteCooldown()+"秒内不可用");
            isVote = false;
        });
        tt = new Thread(() -> {
            $7DTD._Log.info("准备重新恢复时间。");
            int a = serverData.waitTime();
            while (a > 0) {
                a--;
                $7DTD._Log.info("等待 "+a+" 秒后恢复。");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            kitNet.restart(serverData.actualRestartTime());
            isCooldown = false;
        });
    }

    public void login() {
        $7DTD._Log.info("开始登录Ket");
        if (kitNet.loginUser()) $7DTD._Log.info(serverData.serverName() + " Ket登录成功：accessToken=" + kitNet.getToken());
        else $7DTD._Log.error(serverData.serverName() + " Ket登录失败");
        $7DTD._Log.info("开始登录Bot");
        if (botNet.loginUser()) $7DTD._Log.info(serverData.serverName() + " Bot登录成功：session_key=" + botNet.getToken());
        else $7DTD._Log.error(serverData.serverName() + " Bot登录失败");
        $7DTD._Log.info(serverData.serverName() + " AC登录状态：" + acNet.isLogin());
        heartbeat.start();
    }

    public boolean commandIsEnable(String command){
        return this.command.getOrDefault(command,false);
    }


    public void sendChatMsg(String... msgs){
        //_Log.info();
        for (String msg:msgs) botNet.sendPublicMsg(msg);
    }
    public KitData.PlayerInfo[] getPlayerList(){
        String response = Net.sendGetData(this.onlinePlayerUrl);
        $7DTD._Log.debug(response);
        Gson gson = new Gson();
        return gson.fromJson(response, KitData.PlayerInfo[].class);
    }

    public void restart(){
        restartServer();
    }



    public void restartServer(){
        if (isCooldown) return;
        $7DTD._Log.info("即将设置服务器重启");
        isCooldown = true;
        kitNet.restart(serverData.clearSetTime());
        tt.start();
    }

    public void runKitExe() {
        $7DTD._Log.info(serverData.kitExePath());
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(serverData.kitExePath());
            Process process = processBuilder.start();
            //int exitCode = process.waitFor();
            Thread.sleep(10000);
            //System.out.println("进程退出码: " + exitCode);
        } catch (IOException | InterruptedException e) {
            $7DTD._Log.error(e.getMessage());
        }
    }

    public void bind(KitData.Msg msg){
        if (bindUser.containsKey(msg.p())){
            String qq = bindUser.get(msg.p());
            UserConfig config = getUserData(qq);
            config.setBindDone(msg.p());
            $7DTD._Log.info("玩家 "+msg.pn()+" 绑定成功,QQ号为 "+qq);
            botNet.sendPrivateMsg(msg.p(), "绑定成功,QQ号为 "+qq);
        }
    }









    public void voteCheck(){
        int a = 0;
        KitData.PlayerInfo[] list = getPlayerList();
        for (KitData.PlayerInfo info : list) {
            if (voteList.contains(info.steamid()))a++;
        }
        if (a >= Math.round(list.length * serverData.voteScale())) {
            isVote = false;
            voteList.clear();
            $7DTD._Log.info("投票结束");
            sendChatMsg("投票结束即将清理服务器!!!");
            restart();
        }
    }
    public void voteClear(KitData.Msg msg) {

        if (!isVote && System.currentTimeMillis() - voteStartTime < serverData.voteCooldown() * 1000L) {
            $7DTD._Log.info("投票冷却中");
            botNet.sendPrivateMsg(msg.p(), "投票冷却中,请耐心等待");
            return;
        }
        if (voteList.contains(msg.p())) {
            $7DTD._Log.info("玩家【" + msg.pn() + "】已经投过票");
            return;
        }
        List<String> playerMsg = new ArrayList<>();
        if (voteHeart.isAlive()) {
            playerMsg.add("玩家【" + msg.pn() + "】参与了投票");
            playerMsg.add("当前已投票人数:" + voteList.size());
            playerMsg.add("如果您也想清理服务器,请发送【清理】");
        } else {
            int playerNumber = getPlayerList().length;
            playerMsg.add("玩家【" + msg.pn() + "】发起了清理投票");
            playerMsg.add("服务器总人数:" + playerNumber);
            playerMsg.add("清理至少需要" + Math.round(playerNumber * serverData.voteScale()) + "个人参与.");
            playerMsg.add("如果您也想清理服务器,请发送【清理】");
            isVote = true;
            voteStartTime = System.currentTimeMillis();
            voteHeart.start();
        }
        voteList.add(msg.p());
        $7DTD._Log.info(playerMsg.toArray(String[]::new));
        sendChatMsg(playerMsg.toArray(String[]::new));
        voteCheck();

    }




    public String getServerStatus(int status){
        return switch (status){
            case 1 -> "运行中";
            case 2 -> "重启中";
            case 3 -> "已关闭";
            default -> "未知";
        };
    }

    public void help(KitData.Msg m) {
        botNet.sendPrivateMsg(m.p(),"====服务器指令====");
        botNet.sendPrivateMsg(m.p(),"【清理】发起投票清理");
        botNet.sendPrivateMsg(m.p(),"【绑定账号】绑定QQ账号,需要先在QQ内发送");
    }

    public boolean sendServerCommand(String msg){
        $7DTD._Log.debug(msg);
        String c = this.commandUrl+"&command="+ Net.urlEncode(msg);
        $7DTD._Log.debug(c);
        String response = Net.sendGetData(c);
        $7DTD._Log.debug(response);

        Gson gson = new Gson();
        KitData.Command res = gson.fromJson(response, KitData.Command.class);
        $7DTD._Log.debug(res.result());

        return res.result().equals("1");
    }














}
