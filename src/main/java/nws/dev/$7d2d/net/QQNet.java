package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import nws.dev.$7d2d.command.ServerCommand;
import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.config.SingInConfig;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class QQNet {
    public static void listen() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Config.I.getDatas().listenPort), 0);
        _Log.info("HTTP 服务器启动，正在监听端口 "+ Config.I.getDatas().listenPort);
        server.createContext("/", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                _Log.debug(json);
                String response = "false";
                Gson gson = new Gson();
                QQData.Message message = gson.fromJson(json, QQData.Message.class);
                if (message != null) {
                    if (message.user_id.isEmpty() || message.group_id.isEmpty()) return;
                    _Log.debug("收到来自 " + message.user_id + " 的消息");


                    boolean canRun = !usualMsg(message);
                    if (canRun) canRun = !exMsg(message);
                    if (canRun && Config.I.getDatas().adminQQ.contains(message.user_id)) {
                        if (adminMsg(message)) response = "true";
                    } else response = "true";
                }
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 方法不被允许
            }
        });
        server.start();
    }

    private static boolean exMsg(QQData.Message message) {
        String[] parts = message.raw_message.split(" ");
        if (parts.length == 2) {
            return switch (parts[0]) {
                case "领取礼包" -> {
                    _Log.info("领取礼包");
                    UserConfig config = new UserConfig(message.user_id);
                    if (config.isBind()){
                        _Log.debug("已绑定账号");
                        if (config.isReward(parts[1]))
                            QQHelper.easySendGroupMsg(message.group_id,"您已领取过【"+parts[1]+"】礼包");
                        else {
                            QQHelper.easySendGroupMsg(message.group_id,BotNet.giveReward(config,parts[1]));
                        }
                    }else {
                        _Log.debug("未绑定账号");
                        QQHelper.easySendGroupMsg(message.group_id,"未绑定账号，请先绑定账号");
                    }
                    yield true;
                }
                case "查信息" -> {
                    _Log.info("查看玩家信息");
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerByName(parts[1]);
                    if (info == null){
                        _Log.debug("未找到玩家");
                        QQHelper.easySendGroupMsg(message.group_id,"未找到玩家，请确认玩家是否在线");
                    }else {
                        QQHelper.easySendGroupMsg(message.group_id,info.toString());
                    }
                    yield true;
                }
                default -> false;
            };
        }
        return false;
    }

    public static HashMap<String, String> bindUser = new HashMap<>();
    public static boolean usualMsg(QQData.Message message){
        return switch (message.raw_message) {
            case "帮助" -> {
                _Log.info("获取帮助");
                String msg = "-----普通指令-----\\n绑定账号\\n服务器状态\\n签到\\n领取新手礼包\\n领取礼包 【礼包名】\\n查信息 （玩家名）\\n自杀";
                if (Config.I.getDatas().adminQQ.contains(message.user_id)) {
                    msg += "\\n-----管理指令-----\\n重启服务器\\n清理服务器\\n关闭网关\\n启动服务器\\n运行kit";
                }
                msg += "\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。";
                QQHelper.easySendGroupMsg(message.group_id, msg);
                yield true;
            }
            case "绑定账号" -> {
                _Log.info("绑定账号");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()){
                    _Log.info("已绑定账号");
                    QQHelper.sendGroupMsg(message.group_id,QQData.Msg.create(QQData.MsgType.Text,"text",At(message.user_id)+"您已绑定账号"));
                }else {
                    _Log.info("未绑定账号");
                    String name = message.sender.card();
                    if (name.isEmpty()) name = message.sender.nickname();
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerByName(name);
                    if (info == null){
                        _Log.info("未找到玩家");
                        QQHelper.easySendGroupMsg(message.group_id,At(message.user_id)+"未找到玩家，请确认您的昵称（"+name+"）与游戏昵称是否一致，注意大小写，并且是否在线");
                    }else {
                        config.setBind(info.userid());
                        bindUser.put(info.platformid(), message.user_id);
                        QQHelper.easySendGroupMsg(message.group_id, At(message.user_id)+"绑定已准备就绪，请在服务器重启之前在游戏内发送【绑定账号】来完成绑定");
                    }
                }
                yield true;
            }
            case "服务器状态" -> {
                _Log.info("获取服务器状态");
                String s = ServerCommand.getServerInfo();
                _Log.debug(s);
                QQHelper.easySendGroupMsg(message.group_id,s);
                yield true;
            }
            case "领取新手礼包" -> {
                _Log.info("领取新手礼包");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()){
                    _Log.debug("已绑定账号");
                    if (config.isReward("NewbiePack"))
                        QQHelper.easySendGroupMsg(message.group_id,"您已领取过新手礼包");
                    else {
                        QQHelper.easySendGroupMsg(message.group_id,BotNet.giveReward(config,"NewbiePack"));
                    }
                }else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupMsg(message.group_id,"未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "签到" -> {
                _Log.info("签到");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    SingInConfig singInConfig = new SingInConfig(message.user_id);
                    String s = singInConfig.sign(config.getSteamID());
                    _Log.debug(s);
                    QQHelper.easySendGroupMsg(message.group_id, s);
                } else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupMsg(message.group_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "自杀" -> {
                _Log.info("自杀");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    if (BotNet.killPlayer(config.getSteamID())){
                        _Log.debug("自杀成功");
                        QQHelper.easySendGroupMsg(message.group_id, "自杀成功");
                    }else QQHelper.easySendGroupMsg(message.group_id, "自杀失败，请稍后再试");
                } else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupMsg(message.group_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "查信息" -> {
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                    if (info == null) {
                        _Log.debug("未找到玩家");
                        QQHelper.easySendGroupMsg(message.group_id, "未找到玩家，请确认玩家是否在线");
                    } else {
                        QQHelper.easySendGroupMsg(message.group_id, info.toString());
                    }
                }else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupMsg(message.group_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            default -> false;
        };
    }

    public static String At(String id){
        return "";//"[CQ:at,qq="+id+"] ";
    }




    public static Thread restartThread = new Thread(() -> {
        int time = Config.I.getDatas().restartTime;
        BotNet.sendPublicMsg("即将重启服务器，请您做好准备以防数据丢失。");
        while (time > 0) {
            time--;
            switch (time) {
                case 0: BotNet.sendPublicMsg("重启服务器中。。。");
                case 1: BotNet.sendPublicMsg("1 秒后开始重启，建议原地等待。");
                case 2: BotNet.sendPublicMsg("2 秒后开始重启，建议原地等待。");
                case 3: BotNet.sendPublicMsg("3 秒后开始重启，建议原地等待。");
                case 4: BotNet.sendPublicMsg("4 秒后开始重启，建议原地等待。");
                case 5: BotNet.sendPublicMsg("5 秒后开始重启，建议原地等待。");
                case 10: BotNet.sendPublicMsg("10 秒后开始重启，建议原地等待。");
                case 15: BotNet.sendPublicMsg("15 秒后开始重启，建议原地等待。");
                case 20: BotNet.sendPublicMsg("20 秒后开始重启，建议原地等待。");
                case 25: BotNet.sendPublicMsg("25 秒后开始重启，建议原地等待。");
                case 30: BotNet.sendPublicMsg("30 秒后开始重启，建议原地等待。");
                case 35: BotNet.sendPublicMsg("35 秒后开始重启，建议原地等待。");
                case 40: BotNet.sendPublicMsg("40 秒后开始重启，建议原地等待。");
                case 45: BotNet.sendPublicMsg("45 秒后开始重启，建议原地等待。");
                case 60: BotNet.sendPublicMsg("60 秒后开始重启，建议原地等待。");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        _Log.warn("开始重启");
        KitNet.stopNet();
        runKitExe();
        KitNet.startServer();
    });

    public static void runKitExe() {
        _Log.info(Config.I.getDatas().kitExePath);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(Config.I.getDatas().kitExePath);
            Process process = processBuilder.start();
            //int exitCode = process.waitFor();
            Thread.sleep(10000);
            //System.out.println("进程退出码: " + exitCode);
        } catch (IOException | InterruptedException e) {
            _Log.error(e.getMessage());
        }
    }

    public static long wt = 0;
    public static boolean adminMsg(QQData.Message s){
        return switch (s.raw_message) {
            case "重启服务器","关闭网关","启动服务器","运行kit" -> {
                if (System.currentTimeMillis() - wt < 60000) {
                    if (restartThread.isAlive())QQHelper.easySendGroupMsg(s.group_id,"重启进程运行中，请勿执行此指令");
                    else switch (s.raw_message){
                        case "重启服务器" -> {
                            QQHelper.easySendGroupMsg(s.group_id, "即将完全重启服务器");
                            restartThread.start();

                        }
                        case "关闭网关" ->{
                            QQHelper.easySendGroupMsg(s.group_id, "即将关闭网关");
                            KitNet.stopNet();
                        }
                        case "启动服务器" ->{
                            QQHelper.easySendGroupMsg(s.group_id, "即将启动服务器");
                            KitNet.startServer();
                        }
                        case "运行kit" ->{
                            QQHelper.easySendGroupMsg(s.group_id, "即将运行kit");
                            runKitExe();
                        }
                    }
                }else {
                    QQHelper.easySendGroupMsg(s.group_id, "您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令");
                    wt = System.currentTimeMillis();
                }
                yield true;
            }
            case "清理服务器" -> {
                ServerCommand.restart();
                yield true;
            }
            case "重新加载配置" -> {
                ServerCommand.reloadConfig();
                yield true;
            }
            default -> false;
        };
    }

    public static boolean isCooldown = false;
    private static final Thread tt = new Thread(() -> {
        _Log.info("准备重新恢复时间。");
        int a = Config.I.getDatas().waitTime;
        while (a > 0) {
            a--;
            _Log.info("等待 "+a+" 秒后恢复。");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        KitNet.restart(Config.I.getDatas().actualRestartTime);
        isCooldown = false;
    });
    public static void restartServer(){
        if (isCooldown) return;
        _Log.info("即将设置服务器重启");
        isCooldown = true;
        KitNet.restart(Config.I.getDatas().clearSetTime);
        tt.start();
    }
}
