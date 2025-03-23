package nws.dev.$7d2d.command;

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
import nws.dev.$7d2d.system._Log;

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
            "查询白名单 【白名单名】",
            "申请白名单 【白名单名】",
            "申请解封",
            "同意解封 【QQ】",
            "申请跟档",
            "提取物品"
    };
    private static final String[] AdminCommand = {
            "同意跟档 【@qq】",
            "查看跟档物品 【@qq】",
            "重启服务器",
            "清理服务器",
            "关闭网关",
            "启动服务器",
            "运行kit",
            "测试kit"
    };
    private static final StringBuilder HelpMsg = new StringBuilder();
    private static final StringBuilder AdminHelpMsg = new StringBuilder();

    public static final HashMap<String, List<PlayerInfoData.ItemData>> playerSaveItem = new HashMap<>();
    public static final HashMap<String, Integer> playerSaveItemIndex = new HashMap<>();

    public static HashMap<String, String> bindUser = new HashMap<>();
    public static HashMap<String, List<String>> banUser = new HashMap<>();
    public static HashMap<String, Long> saveItem = new HashMap<>();

    static {
        HelpMsg.append("-----当前支持指令-----");
        for (String s : UsualCommand) {
            HelpMsg.append("\n").append(s);
        }
        AdminHelpMsg.append("-----管理员指令-----");
        for (String s : AdminCommand) {
            HelpMsg.append("\n").append(s);
        }
    }
    private final String msg;
    public QQUsualCommand(QQData.Message message){
        super(message);
        this.msg = message.raw_message;
    }
    @Override
    public boolean check(){

        return switch (this.msg) {
            case "帮助" -> help();
            case "管理指令" -> adminHelp();
            case "签到帮助" -> {
                _Log.info("获取签到帮助");
                sendGroupReplyMsg( "签到时您可以@一位玩家来绑定。如果对方当日签到并且您在线，则可以额外获得一次签到奖励。若其当日未签到，您将无法获取下次签到奖励");
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
                        sendGroupReplyMsg( "未找到玩家，请确认玩家是否在线");
                    }else {
                        if (KitNet.kick(config.getSteamID())) {
                            _Log.debug("强制下线成功");
                            sendGroupReplyMsg( "强制下线成功");
                        } else
                            sendGroupReplyMsg( "强制下线失败，请稍后再试");
                    }

                }else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "白名单列表" -> {
                _Log.info("获取白名单列表");
                StringBuilder s = new StringBuilder("-----白名单列表-----");
                ACItemsConfig.I.getDatas().forEach((k, v)->s.append("\\n").append(k));
                sendGroupReplyMsg( s.toString());
                yield true;

            }
            case "活动列表" ->{
                _Log.info("获取活动列表");
                StringBuilder s = new StringBuilder("-----活动列表-----");
                EventListConfig.I.getDatas().forEach((k, v)->s.append("\\n[").append(k).append("]:").append(v));
                sendGroupReplyMsg( s.toString());
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
                        sendGroupReplyMsg("未找到玩家，请确认您的昵称（"+name+"）与游戏昵称是否一致，注意大小写，并且是否在线");
                    }else {
                        config.setBind(info.userid());
                        if (Config.I.getDatas().bindNeedGameMsg){
                            bindUser.put(info.platformid(), message.user_id);
                            sendGroupReplyMsg( "绑定已就绪，请在游戏内发送【绑定账号】来完成绑定。");
                        }else {
                            config.setBindDone(info.platformid());
                            sendGroupReplyMsg( "绑定成功");
                        }
                    }
                }
                yield true;
            }
            case "服务器状态","服务器信息" -> {
                _Log.info("获取服务器状态");
                String s = ServerCommand.getServerInfo();
                _Log.debug(s);
                sendGroupReplyMsg( s);
                yield true;
            }
            case "领取新手礼包" -> {
                _Log.info("领取新手礼包");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()){
                    _Log.debug("已绑定账号");
                    if (config.isReward("NewbiePack"))
                        sendGroupReplyMsg( "您已领取过新手礼包");
                    else {
                        sendGroupReplyMsg( BotNet.giveReward(config,"NewbiePack"));
                    }
                }else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
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
                    sendGroupReplyMsg( s);
                } else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
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
                        sendGroupReplyMsg( "未找到玩家，请确认玩家是否在线");
                    }else {
                        if (ServerHelper.sendServerCommand("kill "+info.entityid())) {
                            _Log.debug("自杀成功");
                            sendGroupReplyMsg( "自杀成功");
                        } else
                            sendGroupReplyMsg( "自杀失败，请稍后再试");
                    }
                } else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "查自己" -> {
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
                    if (info == null) {
                        _Log.debug("未找到玩家");
                        sendGroupReplyMsg( "未找到玩家，请确认玩家是否在线");
                    } else {
                        sendGroupReplyMsg( info.toString());
                    }
                }else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "申请解封" -> {
                _Log.info("申请解封");
                UserConfig config = new UserConfig(message.user_id);
                if (config.isBind()) {
                    if (banUser.containsKey(message.user_id)){
                        _Log.debug("已申请解封");
                        sendGroupReplyMsg( "您已申请过解封，当前已有"+banUser.get(message.user_id).size()+"个群员为您同意解封");
                        yield true;
                    }
                    KitData.BanUser ban = KitNet.getBan(config.getSteamID());
                    if (ban != null){
                        String s = "查询到您的封禁记录\\n";
                        s += "封禁原因："+ OtherHelper.removeColorCodes(ban.banreason())+"\\n";
                        s += "如果你想解封需要"+Config.I.getDatas().unBanNum+"个已绑定账号的群员发送【同意解封 "+message.user_id+"】来解封。";
                        banUser.put(message.user_id,new ArrayList<>());
                        sendGroupReplyMsg( s);
                    }else sendGroupReplyMsg( "未查询到您的封禁记录");
                } else {
                    _Log.debug("未绑定账号");
                    sendGroupReplyMsg( "未绑定账号，请先绑定账号");
                }
                yield true;
            }
            case "申请跟档" -> requestSaveItem();
            case "提取物品" -> sendSaveItem();
            case "下一页" -> {
                if (isAdmin()){
                    if (playerSaveItem.containsKey(this.qq)){
                        List<PlayerInfoData.ItemData> itemDatas = playerSaveItem.get(this.qq);
                        boolean has = true;
                        int index = playerSaveItemIndex.get(this.qq);
                        int maxIndex = index + 20;
                        StringBuilder s = new StringBuilder("--------------第"+(index / 20 +1)+"页------------");
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
                        sendGroupReplyMsg( s.toString());
                    }
                }
                yield true;
            }
            default -> false;
        };
    }

    private boolean adminHelp() {
        _Log.info("获取帮助");
        sendGroupReplyMsg( AdminHelpMsg + "\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        return true;
    }

    public boolean help() {
        _Log.info("获取帮助");
        sendGroupReplyMsg( HelpMsg + "\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        return true;
    }


    public boolean sendSaveItem() {
        UserConfig config = new UserConfig(this.qq);
        if (!config.isBind()) {
            sendGroupReplyMsg("未绑定账号，请先绑定账号");
            return true;
        }
        if (!config.getDatas().canExtractSaveItem){
            sendGroupReplyMsg("无法提取物品，请等待服主核验。");
            return true;
        }
        UserConfig.RecordItem recordItem = config.getRecordItem();
        if (recordItem.getDatas().isEmpty()) {
            sendGroupReplyMsg("没有需要提取的物品");
            return true;
        }
        BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info == null) {
            sendGroupReplyMsg("请在线后再试");
            return true;
        }
        List<PlayerInfoData.ItemData> success = new ArrayList<>();
        recordItem.getDatas().forEach((itemData) -> {
            if (BotNet.give_Item(config.getSteamID(), itemData.i(), itemData.c(), itemData.q())){
                success.add(itemData);
            }
        });
        recordItem.getDatas().removeAll(success);
        recordItem.save();
        config.getDatas().canExtractSaveItem = false;
        config.save();
        sendGroupReplyMsg("成功提取"+success.size()+"个物品");
        return true;
    }

    public boolean requestSaveItem() {
        UserConfig config = new UserConfig(this.qq);
        if (!config.isBind()) {
            sendGroupReplyMsg("未绑定账号，请先绑定账号");
            return true;
        }
        UserConfig.RecordItem recordItem = config.getRecordItem();
        if (!recordItem.getDatas().isEmpty()) {
            sendGroupReplyMsg("您已申请过跟档，如果想要修改，请先提取物品。");
            return true;
        }
        int recordItemLimit = Config.I.getDatas().recordItemDefault + config.getRecordItemLimit();
        if (System.currentTimeMillis() - saveItem.getOrDefault(this.qq, 0L) > 30) {
            saveItem.put(this.qq, System.currentTimeMillis());
            sendGroupReplyMsg("请确认是否已将物品上的模组卸下，您可以跟档" + recordItemLimit + "个物品，如果确认无误请再次发送此指令。");
            return true;
        }
        saveItem.remove(this.qq);
        BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info != null) {
            sendGroupReplyMsg("请离线后再试");
            return true;
        }
        PlayerInfoData playerInfoData = KitNet.getBagItems(KitNet.formatSteamId(config.getSteamID()));
        if (playerInfoData == null) {
            sendGroupReplyMsg("获取背包物品失败，请重试");
            return true;
        }
        if (playerInfoData.bag().size() > recordItemLimit) {
            sendGroupReplyMsg("背包物品过多，请清理后再试");
            return true;
        }
        if (playerInfoData.bag().isEmpty()) {
            sendGroupReplyMsg("背包物品为空，请检查");
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
        sendGroupReplyMsg("物品记录完成，成功:" + success + " ，失败:" + (items.size() - success) + "。");
        return true;
    }
}
