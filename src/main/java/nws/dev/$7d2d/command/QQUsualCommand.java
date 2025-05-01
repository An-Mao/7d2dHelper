package nws.dev.$7d2d.command;

import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.config.*;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.data.PlayerInfoData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.helper.ServerHelper;
import nws.dev.$7d2d.net.BotNet;
import nws.dev.$7d2d.net.KitNet;
import nws.dev.$7d2d.system._File;
import nws.dev.$7d2d.system._Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QQUsualCommand extends QQCommand{
    private static final String[] UsualCommand = {
            "绑定账号",
            "签到帮助",
            "踢自己",
            "活动列表",
            "服务器状态",
            "签到",
            "领取新手礼包",
            "领取礼包 【礼包名】",
            "查自己",
            "查信息 （玩家名）",
            "自杀",
            "白名单列表",
            "查找物品 【物品名】",
            "查询物品 【物品名】",
            "查询配方 【物品名】",
            "查找白名单 【物品名】",
            "查询白名单 【白名单名】",
            "申请白名单 【白名单名】",
            "申请解封",
            "同意解封 【QQ】",
            "申请跟档",
            "提取物品",
            "购买跟档物品数量 【数量】"
    };
    private static final String[] AdminCommand = {
            "同意跟档@qq",
            "查看跟档物品@qq"
    };
    private static final String[] PrivateCommand = {
            "申请跟档",
            "提取物品"
    };
    private static final String[] PrivateAdminCommand = {
            "重新读取游戏文件",
            "待审核跟档列表",
            "同意跟档 qq",
            "查看跟档物品 qq",
            "重启服务器",
            "清理服务器",
            "关闭网关",
            "启动服务器",
            "运行kit",
            "测试kit"
    };
    private static final StringBuilder HelpMsg = new StringBuilder();
    private static final StringBuilder AdminHelpMsg = new StringBuilder();
    private static final StringBuilder QAHelp = new StringBuilder(); // QAHelp
    private static final StringBuilder PrivateHelpMsg = new StringBuilder();
    private static final StringBuilder PrivateAdminHelpMsg = new StringBuilder();

    public static final HashMap<String, List<PlayerInfoData.ItemData>> playerSaveItem = new HashMap<>();
    public static final HashMap<String, Integer> playerSaveItemIndex = new HashMap<>();

    public static HashMap<String, String> bindUser = new HashMap<>();
    public static HashMap<String, List<String>> banUser = new HashMap<>();
    public static HashMap<String, Long> saveItem = new HashMap<>();


    public static long wt = 0;

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

    static {
        HelpMsg.append("-----当前支持指令-----");
        for (String s : UsualCommand) {
            HelpMsg.append("\\n").append(s);
        }
        AdminHelpMsg.append("-----管理员指令-----");
        for (String s : AdminCommand) {
            AdminHelpMsg.append("\\n").append(s);
        }
        PrivateHelpMsg.append("-----当前支持指令-----");
        for (String s : PrivateCommand) {
            PrivateHelpMsg.append("\\n").append(s);
        }
        PrivateAdminHelpMsg.append("-----管理员指令-----");
        for (String s : PrivateAdminCommand) {
            PrivateAdminHelpMsg.append("\\n").append(s);
        }
        QA.I.getDatas().keySet().forEach(s -> QAHelp.append("\\n").append(s));
    }
    private final String msg;
    public QQUsualCommand(QQData.Message message){
        super(message);
        this.msg = message.raw_message;
    }

    @Override
    public boolean privateMsg() {
        return switch (this.msg) {
            case "帮助" -> privateHelp();
            case "重新读取游戏文件"-> {
                if (isAdmin()) {
                    sendMsg("开始重新读取游戏文件");
                    GameInfo.I.init();
                    sendMsg("重新读取游戏文件完成");
                }
                yield true;
            }
            case "管理指令" -> privateAdminHelp();
            case "申请跟档" -> requestSaveItem();
            case "提取物品" -> sendSaveItem();
            case "重启服务器","关闭网关","启动服务器","运行kit" -> {
                if (isAdmin()) {
                    if (System.currentTimeMillis() - wt < 60000) {
                        if (restartThread.isAlive()) sendMsg("重启进程运行中，请勿执行此指令");
                        else switch (msg) {
                            case "重启服务器" -> {
                                sendMsg("即将完全重启服务器");
                                restartThread.start();

                            }
                            case "关闭网关" -> {
                                sendMsg("即将关闭网关");
                                KitNet.stopNet();
                            }
                            case "启动服务器" -> {
                                sendMsg("即将启动服务器");
                                KitNet.startServer();
                            }
                            case "运行kit" -> {
                                sendMsg("即将运行kit");
                                runKitExe();
                            }
                        }
                    } else {
                        sendMsg("您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令");
                        wt = System.currentTimeMillis();
                    }
                }
                yield true;
            }
            case "测试Kit" ->{
                if (isAdmin()) {
                    String token = KitNet.getToken();
                    if (token != null && !token.isEmpty()) {
                        sendMsg("kit连接正常");
                    } else {
                        sendMsg("kit连接异常，已尝试重新连接");
                    }
                }
                yield true;
            }
            case "清理服务器" -> {
                if (isAdmin()) {
                    ServerCommand.restart();
                }
                yield true;
            }
            case "重新加载配置" -> {
                if (isAdmin()) {
                    ServerCommand.reloadConfig();
                }
                yield true;
            }
            case "待审核跟档列表" -> pendingSaveItemList();
            default -> false;
        };
    }

    private boolean pendingSaveItemList() {
        if (isAdmin()) {
            StringBuilder sb = new StringBuilder("------------");
            _File.getFiles(DataTable.UserDir, ".json").forEach(p->{
                String uqq = p.getFileName().toString().replace(".json","");
                UserConfig config = new UserConfig(uqq);
                if (!config.getDatas().canExtractSaveItem && !config.getRecordItem().getDatas().isEmpty()){
                    sb.append("\\n【").append(uqq).append("】 ").append(config.getRecordItem().getDatas().size());
                }
            });
            sendMsg(sb.toString());
        }
        return true;
    }

    private boolean privateAdminHelp() {
        sendMsg( PrivateAdminHelpMsg + "\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        return true;
    }
    private boolean privateHelp() {
        _Log.info("获取帮助");
        sendMsg( PrivateHelpMsg + "\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        return true;
    }
    @Override
    public boolean groupMsg() {
        return switch (this.msg) {
            case "帮助" -> help();
            case "管理指令" -> adminHelp();
            case "签到帮助" -> signHelp();
            case "踢自己"-> kickSelf();
            case "白名单列表" -> whiteList();
            case "活动列表" -> eventList();
            case "绑定账号" -> bind();
            case "服务器状态","服务器信息" -> serverInfo();
            case "领取新手礼包" -> getNewPlayerGift();
            case "签到" -> sign();
            case "自杀" -> killSelf();
            case "查自己" -> lookSelf();
            case "申请解封" -> requestUnban();
            case "申请跟档" -> requestSaveItem();
            case "提取物品" -> sendSaveItem();
            case "下一页" -> nextPage();
            default -> false;
        };
    }
    private boolean nextPage() {
        if (isAdmin()){
            if (playerSaveItem.containsKey(this.qq)){
                List<PlayerInfoData.ItemData> itemDatas = playerSaveItem.get(this.qq);
                boolean has = true;
                int index = playerSaveItemIndex.get(this.qq);
                int maxIndex = index + 20;
                StringBuilder s = new StringBuilder("----第"+(index / 20 +1)+"页----");
                for (int i = index; i < maxIndex; i++) {
                    if (i >= itemDatas.size()){
                        has = false;
                        break;
                    }
                    PlayerInfoData.ItemData itemData = itemDatas.get(i);
                    s.append("\\n").append(OtherHelper.removeColorCodes(itemData.n())).append(" x ").append(itemData.c());
                }
                if (has){
                    playerSaveItemIndex.put(this.qq,maxIndex);
                    s.append("\\n...\\n发送【下一页】继续查看");
                }else {
                    QQUsualCommand.playerSaveItem.remove(this.qq);
                    QQUsualCommand.playerSaveItemIndex.remove(this.qq);
                    s.append("\\n---没有更多了---");
                }
                sendMsg( s.toString());
            }
        }
        return true;
    }
    public boolean requestUnban() {
        if (CommandConfig.I.isEnable("requestUnban")) return false;
        _Log.info("申请解封");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            if (banUser.containsKey(this.qq)){
                _Log.debug("已申请解封");
                sendMsg( "您已申请过解封，当前已有"+banUser.get(this.qq).size()+"个群员为您同意解封");
                return true;
            }
            KitData.BanUser ban = KitNet.getBan(config.getSteamID());
            if (ban != null){
                String s = "查询到您的封禁记录\\n";
                s += "封禁原因："+ OtherHelper.removeColorCodes(ban.banreason())+"\\n";
                s += "如果你想解封需要"+Config.I.getDatas().unBanNum+"个已绑定账号的群员发送【同意解封 "+this.qq+"】来解封。";
                banUser.put(this.qq,new ArrayList<>());
                sendMsg( s);
            }else sendMsg( "未查询到您的封禁记录");
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
    private boolean lookSelf() {
        if (CommandConfig.I.isEnable("lookSelf")) return false;
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                _Log.debug("未找到玩家");
                sendMsg( "未找到玩家，请确认玩家是否在线");
            } else {
                sendMsg( info.toString());
            }
        }else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
    private boolean killSelf() {
        if (CommandConfig.I.isEnable("killSelf")) return false;
        _Log.info("自杀");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                _Log.debug("未找到玩家");
                sendMsg( "未找到玩家，请确认玩家是否在线");
            }else {
                if (ServerHelper.sendServerCommand("kill "+info.entityid())) {
                    _Log.debug("自杀成功");
                    sendMsg( "自杀成功");
                } else
                    sendMsg( "自杀失败，请稍后再试");
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
    private boolean sign() {

        if (CommandConfig.I.isEnable("sign")) return false;
        _Log.info("签到");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            SingInConfig singInConfig = new SingInConfig(this.qq);
            String s = singInConfig.sign(config.getSteamID());
            _Log.debug(s);
            sendMsg(s);
        } else {
            _Log.debug("未绑定账号");
            sendMsg("未绑定账号，请先绑定账号");
        }
        return true;

    }
    private boolean getNewPlayerGift() {
        if (CommandConfig.I.isEnable("getNewPlayerGift")) return false;
        _Log.info("领取新手礼包");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()){
            _Log.debug("已绑定账号");
            if (config.isReward("NewbiePack"))
                sendMsg( "您已领取过新手礼包");
            else {
                sendMsg( BotNet.giveReward(config,"NewbiePack"));
            }
        }else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
    private boolean serverInfo() {
        if (CommandConfig.I.isEnable("serverInfo")) return false;
        _Log.info("获取服务器状态");
        String s = ServerCommand.getServerInfo();
        _Log.debug(s);
        sendMsg(s);
        return true;

    }
    private boolean bind(){
        if (CommandConfig.I.isEnable("bind")) return false;
        _Log.info("绑定账号");
        UserConfig config = new UserConfig(this.qq);
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
                sendMsg("未找到玩家，请确认您的昵称（"+name+"）与游戏昵称是否一致，注意大小写，并且是否在线");
            }else {
                config.setBind(info.userid());
                if (Config.I.getDatas().bindNeedGameMsg){
                    bindUser.put(info.platformid(), this.qq);
                    sendMsg( "绑定已就绪，请在游戏内发送【绑定账号】来完成绑定。");
                }else {
                    config.setBindDone(info.platformid());
                    sendMsg( "绑定成功");
                }
            }
        }
        return true;
    }
    private boolean eventList() {
        if (CommandConfig.I.isEnable("eventList")) return false;
        _Log.info("获取活动列表");
        StringBuilder s = new StringBuilder("-----活动列表-----");
        EventListConfig.I.getDatas().forEach((k, v) -> s.append("\\n[").append(k).append("]:").append(v));
        sendMsg(s.toString());
        return true;

    }
    private boolean whiteList() {
        if (CommandConfig.I.isEnable("whiteList")) return false;
        _Log.info("获取白名单列表");
        StringBuilder s = new StringBuilder("-----白名单列表-----");
        ACItemsConfig.I.getDatas().forEach((k, v) -> s.append("\\n").append(k));
        sendMsg(s.toString());
        return true;
    }
    public boolean kickSelf() {
        if (CommandConfig.I.isEnable("kickSelf")) return false;
        UserConfig config = new UserConfig(this.qq);
        if (!config.isBind()) {
            _Log.debug("未绑定账号");
            sendMsg("未绑定账号，请先绑定账号");
            return true;
        }
        BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info == null) {
            _Log.debug("未找到玩家");
            sendMsg("未找到玩家，请确认玩家是否在线");
        } else {
            if (KitNet.kick(config.getSteamID())) {
                _Log.debug("强制下线成功");
                sendMsg("强制下线成功");
            } else
                sendMsg("强制下线失败，请稍后再试");
        }
        return true;

    }
    private boolean signHelp() {
        if (CommandConfig.I.isEnable("signHelp")) return false;
        _Log.info("获取签到帮助");
        sendMsg("签到时您可以@一位玩家来绑定。如果对方当日签到并且您在线，则可以额外获得一次签到奖励。若其当日未签到，您将无法获取下次签到奖励");
        return true;
    }
    private boolean adminHelp() {
        if (CommandConfig.I.isEnable("adminHelp")) return false;
        if(isAdmin()) {
            _Log.info("获取帮助");
            sendMsg(AdminHelpMsg + "\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        }
        return true;
    }
    public boolean help() {
        if (CommandConfig.I.isEnable("help")) return false;
        _Log.info("获取帮助");

        sendMsg( HelpMsg+ QAHelp.toString() + "\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        return true;
    }
    public boolean sendSaveItem() {
        if (CommandConfig.I.isEnable("sendSaveItem")) return false;
        UserConfig config = new UserConfig(this.qq);
        if (!config.isBind()) {
            sendMsg("未绑定账号，请先绑定账号");
            return true;
        }
        if (!config.getDatas().canExtractSaveItem){
            sendMsg("无法提取物品，请等待服主核验。");
            return true;
        }
        UserConfig.RecordItem recordItem = config.getRecordItem();
        if (recordItem.getDatas().isEmpty()) {
            sendMsg("没有需要提取的物品");
            return true;
        }
        BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info == null) {
            sendMsg("请在线后再试");
            return true;
        }
        List<PlayerInfoData.ItemData> success = new ArrayList<>();
        recordItem.getDatas().forEach((itemData) -> {
            if (BotNet.giveItem(config.getSteamID(), itemData.n(), itemData.c(), itemData.q())){
                success.add(itemData);
            }
        });
        recordItem.getDatas().removeAll(success);
        recordItem.save();
        config.getDatas().canExtractSaveItem = false;
        config.save();
        sendMsg("成功提取"+success.size()+"个物品");
        return true;
    }
    public boolean requestSaveItem() {
        if (CommandConfig.I.isEnable("requestSaveItem")) return false;
        UserConfig config = new UserConfig(this.qq);
        if (!config.isBind()) {
            sendMsg("未绑定账号，请先绑定账号");
            return true;
        }



        UserConfig.RecordItem recordItem = config.getRecordItem();
        if (!recordItem.getDatas().isEmpty()) {
            sendMsg("您已申请过跟档，如果想要修改，请先提取物品。");
            return true;
        }
        KitData.GsList list = KitNet.getGsList();
        if (list.result() == 1) {
            for (KitData.GsInfo info : list.list()) {
                if (info.status() != 1) {
                    sendMsg("服务器状态异常，请稍后再试");
                    return true;
                }
            }
        }else {
            sendMsg("获取服务器状态异常，请稍后再试");
            return true;
        }
        int recordItemLimit = Config.I.getDatas().recordItemDefault + config.getRecordItemLimit();
        if (System.currentTimeMillis() - saveItem.getOrDefault(this.qq, 0L) > 30000) {
            saveItem.put(this.qq, System.currentTimeMillis());
            sendMsg("请确认是否已将物品上的模组卸下，您可以跟档" + recordItemLimit + "个物品，如果确认无误请再次发送此指令。");
            return true;
        }
        saveItem.remove(this.qq);
        BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info != null) {
            sendMsg("请离线后再试");
            return true;
        }
        PlayerInfoData playerInfoData = KitNet.getBagItems(KitNet.formatSteamId(config.getSteamID()));
        if (playerInfoData == null) {
            sendMsg("获取背包物品失败，请重试");
            return true;
        }
        if (playerInfoData.bag().size() > recordItemLimit) {
            sendMsg("背包物品过多，请清理后再试");
            return true;
        }
        if (playerInfoData.bag().isEmpty()) {
            sendMsg("背包物品为空，请检查");
            return true;
        }
        List<PlayerInfoData.ItemData> items = playerInfoData.bag();
        recordItem.getDatas().clear();
        items.forEach((itemData) -> {
            if (KitNet.removeBagItems(KitNet.formatSteamId(config.getSteamID()), String.valueOf(itemData.id()))) {
                recordItem.getDatas().add(itemData);
            }
        });
        recordItem.save();
        config.getDatas().canExtractSaveItem = false;
        config.save();
        int success = recordItem.getDatas().size();
        sendMsg("物品记录完成，成功:" + success + " ，失败:" + (items.size() - success) + "。");
        return true;
    }
}
