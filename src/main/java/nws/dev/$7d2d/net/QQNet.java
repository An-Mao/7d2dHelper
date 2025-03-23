package nws.dev.$7d2d.net;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.command.ServerCommand;
import nws.dev.$7d2d.config.*;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.data.PlayerInfoData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.helper.ServerHelper;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

                if (message != null && Config.I.getDatas().qqGroup.contains(message.group_id)) {
                    if (message.user_id.isEmpty() || message.group_id.isEmpty()) return;
                    _Log.debug("收到来自 " + message.user_id + " 的消息");


                    QQAtCommand atCommand = new QQAtCommand(message);
                    boolean canRun = !atCommand.check();
                    QQUsualCommand usualCommand = new QQUsualCommand(message);
                    if (canRun) canRun = !usualCommand.check();
                    QQExCommand exCommand = new QQExCommand(message);
                    if (canRun) canRun = !exCommand.check();
                    if (canRun) canRun = !QAMsg(message);
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

    private static boolean QAMsg(QQData.Message message) {
        String answer = QA.I.getAnswer(message.raw_message);
        if (answer.isEmpty())return false;
        QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id,answer);
        return true;
    }

    /*
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
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "您已领取过【"+parts[1]+"】礼包");
                        else {
                            QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id,BotNet.giveReward(config,parts[1]));
                        }
                    }else {
                        _Log.debug("未绑定账号");
                        QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id,"未绑定账号，请先绑定账号");
                    }
                    yield true;
                }
                case "查信息" -> {
                    _Log.info("查看玩家信息");
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerByName(parts[1]);
                    if (info == null){
                        _Log.debug("未找到玩家");
                        QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id,"未找到玩家，请确认玩家是否在线");
                    }else {
                        QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id,info.toString());
                    }
                    yield true;
                }
                case "申请白名单" -> {
                    _Log.info("申请白名单");
                    UserConfig config = new UserConfig(message.user_id);
                    if (config.isBind()){
                        _Log.debug("已绑定账号");
                        BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                        if (info == null){
                            _Log.debug("未找到玩家");
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未找到玩家，请确认玩家是否在线");
                        }else {
                            ACItemsData data = ACItemsConfig.I.get(parts[1]);
                            if (data == null){
                                _Log.debug("未找到此白名单");
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未找到此白名单，请确认此白名单是否存在");
                            }else {
                                if (data.allNeed()?info.point() >= data.point() && info.level() >= data.level():info.point() >= data.point() || info.level() >= data.level()){
                                    _Log.debug("白名单检测成功");
                                    if (ACNet.I.addWhite(info.userid(),data.getFormatItems())){
                                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "白名单添加成功");
                                    }else {
                                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "白名单添加失败，网络异常");
                                    }
                                }else {
                                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "白名单添加失败，你未满足白名单要求："+"\\n需要等级："+data.level()+"\\n需要积分："+data.point()+"\\n"+(data.allNeed()?"需要等级和积分全部满足":"需要等级或积分任一满足"));

                                }

                            }
                        }
                    }else {
                        _Log.debug("未绑定账号");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                    }
                    yield true;
                }
                case "查询白名单" -> {
                    _Log.info("查询白名单");
                    ACItemsData data = ACItemsConfig.I.get(parts[1]);
                    if (data == null){
                        _Log.debug("未找到此白名单");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未找到此白名单，请确认此白名单是否存在");
                    }else {
                        _Log.debug("白名单查询成功");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, data.toString());
                    }
                    yield true;
                }
                case "同意解封" -> {
                    _Log.info("同意解封");
                    UserConfig config = new UserConfig(message.user_id);
                    if (config.isBind()){
                        _Log.debug("已绑定账号");

                        if (banUser.containsKey(parts[1])){
                            if (parts[1].equals(message.user_id)){
                                _Log.debug("相同玩家");
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "您不能同意自己的解封申请");
                                yield true;
                            }
                            List<String> count = banUser.get(parts[1]);
                            if (count.contains(message.user_id)){
                                _Log.debug("已同意过此玩家的解封申请");
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "您已同意过此玩家的解封申请");
                                yield true;
                            }
                            count.add(message.user_id);
                            banUser.put(parts[1], count);
                            if (count.size() >= Config.I.getDatas().unBanNum){
                                banUser.remove(parts[1]);
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "已满足解封所需人数，即将解除封禁");
                                UserConfig b = new UserConfig(parts[1]);
                                if (KitNet.unBan(b.getSteamID())){
                                    QQHelper.easySendGroupAtMsg(message.group_id,parts[1], "解封成功");
                                }else {
                                    QQHelper.easySendGroupAtMsg(message.group_id, parts[1],"解封失败，网络异常");
                                }
                            }else {
                                _Log.debug("未满足解封所需人数");
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "您已同意此玩家的解封申请");
                            }
                        }else {
                            _Log.debug("未找到此玩家");
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未找到此封禁玩家");
                        }
                    }else {
                        _Log.debug("未绑定账号");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                    }
                    yield true;
                }
                default -> false;
            };
        }
        return false;
    }

     */

    public static HashMap<String, String> bindUser = new HashMap<>();
    public static HashMap<String, List<String>> banUser = new HashMap<>();
    public static HashMap<String, Long> saveItem = new HashMap<>();

    /*
    public static boolean usualMsg(QQData.Message message){
        return switch (message.raw_message) {
            case "帮助" -> {
                _Log.info("获取帮助");
                final StringBuilder msg = new StringBuilder("-----普通指令-----\\n绑定账号\\n签到帮助\\n踢自己\\n活动列表\\n服务器状态\\n签到\\n领取新手礼包\\n领取礼包 【礼包名】\\n查信息 （玩家名）\\n自杀\\n白名单列表\\n查询白名单 【白名单名】\\n申请白名单 【白名单名】\\n申请解封\\n同意解封 【QQ】");
                QA.I.getDatas().keySet().forEach(s -> msg.append("\\n").append(s));
                if (Config.I.getDatas().adminQQ.contains(message.user_id)) {
                    msg.append("\\n-----管理指令-----\\n重启服务器\\n清理服务器\\n关闭网关\\n启动服务器\\n运行kit\\n测试kit");
                }
                msg.append("\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
                QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id, msg.toString());
                yield true;
            }
            case "签到帮助" -> {
                _Log.info("获取签到帮助");
                QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id, "签到时您可以@一位玩家来绑定。如果对方当日签到并且您在线，则可以额外获得一次签到奖励。若其当日未签到，您将无法获取下次签到奖励");
                yield true;
            }
            case "踢自己"->{
                //_Log.info("踢自己");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()){
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                    //_Log.debug("已绑定账号");
                    if (info == null) {
                        _Log.debug("未找到玩家");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未找到玩家，请确认玩家是否在线");
                    }else {
                        if (KitNet.kick(config.getSteamID())) {
                            _Log.debug("强制下线成功");
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "强制下线成功");
                        } else
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "强制下线失败，请稍后再试");
                    }

                }else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "白名单列表" -> {
                _Log.info("获取白名单列表");
                StringBuilder s = new StringBuilder("-----白名单列表-----");
                ACItemsConfig.I.getDatas().forEach((k, v)->s.append("\\n").append(k));
                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, s.toString());
                yield true;

            }
            case "活动列表" ->{
                _Log.info("获取活动列表");
                StringBuilder s = new StringBuilder("-----活动列表-----");
                EventListConfig.I.getDatas().forEach((k,v)->s.append("\\n[").append(k).append("]:").append(v));
                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, s.toString());
                yield true;
            }
            case "绑定账号" -> {
                _Log.info("绑定账号");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()){
                    _Log.info("已绑定账号");
                    QQHelper.sendGroupMsg(message.group_id,QQData.Msg.create(QQData.MsgType.Text,"text","您已绑定账号"));
                }else {
                    _Log.info("未绑定账号");
                    String name = message.sender.card();
                    if (name.isEmpty()) name = message.sender.nickname();
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerByName(name);
                    if (info == null){
                        _Log.info("未找到玩家");
                        QQHelper.easySendGroupReplyMsg(message.group_id,message.message_id,"未找到玩家，请确认您的昵称（"+name+"）与游戏昵称是否一致，注意大小写，并且是否在线");
                    }else {
                        config.setBind(info.userid());
                        if (Config.I.getDatas().bindNeedGameMsg){
                            bindUser.put(info.platformid(), message.user_id);
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "绑定已就绪，请在游戏内发送【绑定账号】来完成绑定。");
                        }else {
                            config.setBindDone(info.platformid());
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "绑定成功");
                        }
                    }
                }
                yield true;
            }
            case "服务器状态","服务器信息" -> {
                _Log.info("获取服务器状态");
                String s = ServerCommand.getServerInfo();
                _Log.debug(s);
                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, s);
                yield true;
            }
            case "领取新手礼包" -> {
                _Log.info("领取新手礼包");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()){
                    _Log.debug("已绑定账号");
                    if (config.isReward("NewbiePack"))
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "您已领取过新手礼包");
                    else {
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, BotNet.giveReward(config,"NewbiePack"));
                    }
                }else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
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
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, s);
                } else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "自杀" -> {
                _Log.info("自杀");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                    if (info == null) {
                        _Log.debug("未找到玩家");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未找到玩家，请确认玩家是否在线");
                    }else {
                        if (ServerHelper.sendServerCommand("kill "+info.entityid())) {
                            _Log.debug("自杀成功");
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "自杀成功");
                        } else
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "自杀失败，请稍后再试");
                    }
                } else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "查信息" -> {
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                    if (info == null) {
                        _Log.debug("未找到玩家");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未找到玩家，请确认玩家是否在线");
                    } else {
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, info.toString());
                    }
                }else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "申请解封" -> {
                _Log.info("申请解封");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    if (banUser.containsKey(message.user_id)){
                        _Log.debug("已申请解封");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "您已申请过解封，当前已有"+banUser.get(message.user_id)+"个群员为您同意解封");
                        yield true;
                    }
                    KitData.BanUser ban = KitNet.getBan(config.getSteamID());
                    if (ban != null){
                        String s = "查询到您的封禁记录\\n";
                        s += "封禁原因："+ban.banreason()+"\\n";
                        s += "如果你想解封需要"+Config.I.getDatas().unBanNum+"个已绑定账号的群员发送【同意解封 "+message.user_id+"】来解封。";
                        banUser.put(message.user_id,new ArrayList<>());
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, s);
                    }else QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未查询到您的封禁记录");
                } else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "申请跟档" ->{
                _Log.debug("申请跟档");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    UserConfig.RecordItem recordItem = config.getRecordItem();
                    if (!recordItem.getDatas().isEmpty()) {
                        _Log.debug("已申请跟档");
                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "您已申请过跟档，如果想要修改，请先提取物品。");
                        yield true;
                    }else {
                        int recordItemLimit = Config.I.getDatas().recordItemLimit + config.getRecordItemLimit();
                        if (System.currentTimeMillis() - saveItem.getOrDefault(message.user_id, 0L) > 30) {
                            saveItem.put(message.user_id, System.currentTimeMillis());
                            QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "请确认是否已将物品上的模组卸下，您可以跟档"+recordItemLimit+"个物品，如果确认无误请再次发送此指令。");
                        } else {
                            saveItem.remove(message.user_id);
                            BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                            if (info == null) {
                                PlayerInfoData playerInfoData = KitNet.getBagItems(KitNet.formatSteamId(config.getSteamID()));
                                if (playerInfoData != null) {
                                    if (playerInfoData.bag().size() > recordItemLimit) {
                                        _Log.debug("背包物品过多");
                                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "背包物品过多，请清理后再试");
                                    } else {
                                        HashMap<String, Integer> items = playerInfoData.getBagItems();
                                        recordItem.getDatas().clear();
                                        recordItem.getDatas().putAll(items);
                                        recordItem.save();
                                        _Log.debug("跟档成功");
                                        QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "跟档成功");
                                    }
                                }
                            } else {
                                _Log.debug("玩家在线");
                                QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "请离线后再试");
                            }
                        }
                    }
                }else {
                    _Log.debug("未绑定账号");
                    QQHelper.easySendGroupReplyMsg(message.group_id, message.message_id, "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            default -> false;
        };
    }

     */

    public static String At(String id){
        return "";//"[CQ:at,steamid="+id+"] ";
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
                    if (restartThread.isAlive())QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "重启进程运行中，请勿执行此指令");
                    else switch (s.raw_message){
                        case "重启服务器" -> {
                            QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "即将完全重启服务器");
                            restartThread.start();

                        }
                        case "关闭网关" ->{
                            QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "即将关闭网关");
                            KitNet.stopNet();
                        }
                        case "启动服务器" ->{
                            QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "即将启动服务器");
                            KitNet.startServer();
                        }
                        case "运行kit" ->{
                            QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "即将运行kit");
                            runKitExe();
                        }
                    }
                }else {
                    QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令");
                    wt = System.currentTimeMillis();
                }
                yield true;
            }
            case "测试Kit" ->{
                String token = KitNet.getToken();
                if (token != null && !token.isEmpty()) {
                    QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "kit连接正常");
                }else {
                    QQHelper.easySendGroupReplyMsg(s.group_id, s.message_id, "kit连接异常，已尝试重新连接");
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
            case "调试信息" ->//
                    true;
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
