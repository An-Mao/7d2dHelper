package nws.dev.$7d2d.command;

import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.config.*;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.ACNet;
import nws.dev.$7d2d.net.BotNet;
import nws.dev.$7d2d.net.KitNet;
import nws.dev.$7d2d.net.Urls;
import nws.dev.$7d2d.system._Byte;
import nws.dev.$7d2d.system._Log;

import java.io.File;
import java.util.List;

public class QQExCommand extends QQCommand {
    private final String command;
    private final String[] args;
    private final String rawArg;

    public QQExCommand(QQData.Message message) {
        super(message);
        if (msg.contains(" ")) {
            this.args = msg.split(" ");
            this.command = this.args[0];
            StringBuilder s = new StringBuilder();
            for (int i = 1; i < this.args.length; i++) {
                if (!s.isEmpty()) s.append(" ");
                s.append(this.args[i]);
            }
            this.rawArg = s.toString();
        } else {
            this.args = new String[0];
            this.command = "";
            this.rawArg = "";
        }
    }

    @Override
    public boolean check() {
        if (command.isEmpty()) return false;
        return super.check();
    }

    @Override
    public boolean privateMsg() {
        return switch (command) {
            case "同意跟档" -> agreeSave();
            case "查看跟档物品" -> checkSaveItem();
            default -> false;
        };
    }

    private boolean checkSaveItem() {
        if (isAdmin()) {
            String target = args[1];
            if (target.isEmpty()) {
                sendMsg("指令格式错误，正确格式：查看跟档物品 qq");
                return false;
            }else {
                UserConfig config = new UserConfig(target);
                if (config.isBind()) {
                    QQUsualCommand.playerSaveItem.remove(this.qq);
                    QQUsualCommand.playerSaveItemIndex.remove(this.qq);
                    UserConfig.RecordItem recordItem = config.getRecordItem();
                    if (recordItem.getDatas().isEmpty()) {
                        sendMsg("对方没有跟档物品");
                    }else {
                        StringBuilder s = new StringBuilder("对方跟档物品:");
                        int[] c = {0};
                        recordItem.getDatas().forEach((itemData) ->{
                            if (c[0] < 20) {
                                c[0]++;
                                s.append("\\n").append(OtherHelper.removeColorCodes(itemData.n())).append(" x ").append(itemData.c());
                            }
                        });
                        if (recordItem.getDatas().size() > 20){
                            QQUsualCommand.playerSaveItem.put(this.qq,recordItem.getDatas());
                            QQUsualCommand.playerSaveItemIndex.put(this.qq,20);
                            s.append("\\n...\\n发送【下一页】继续查看");
                        }
                        sendMsg(s.toString());
                    }
                } else sendMsg("对方未绑定账号");
            }
        }
        return true;
    }

    private boolean agreeSave() {
        if (isAdmin()) {
            String target = args[1];
            if (target.isEmpty()) {
                sendMsg("指令格式错误，正确格式：同意跟档 qq");
                return false;
            }else {
                UserConfig config = new UserConfig(target);
                if (config.isBind()) {
                    config.getDatas().canExtractSaveItem = true;
                    config.save();
                    sendMsg("已同意跟档申请");
                } else sendMsg("对方未绑定账号");
            }
        }
        return true;
    }

    @Override
    public boolean groupMsg() {
        return switch (command) {
            case "领取礼包" -> receiveGift();
            case "查信息" -> requestInfo();
            case "申请白名单" -> applyWhite();
            case "查询白名单" -> requestWhite();
            case "同意解封" -> agreeUnban();
            case "购买跟档物品数量" -> buySaveItemNum();
            case "查找白名单" -> findWhite();
            case "查询物品" -> getItemInfo();
            case "查找物品" -> findItem();
            case "查询配方" -> requestRecipe();
            default -> false;
        };
    }

    private boolean requestRecipe() {
        if (CommandConfig.I.isEnable("requestRecipe")) return false;
        if (args[1].isEmpty()){
            sendMsg("物品名称不能为空");
            return false;
        }
        _Log.info("查询配方");
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (s.isEmpty()) s.append(args[i]);
            else s.append(" ").append(args[i]);
        }
        String r = GameInfo.I.getRecipeInfo(s.toString());
        if (Config.I.getDatas().imageRecipes){
            String md5 = DataTable.ImageCache+"/"+_Byte.getMD5(r)+".png";
            /*
            HashMap<String,String> map = new HashMap<>();
            map.put("file",convertPath(md5));
            map.put("subType","0");
            map.put("id","40000");
            _Log.debug(r);

             */

            if (!new File(md5).exists()) DrawConfig.I.createImage(List.of(r.split("\\\\n"))).save(md5);
            QQHelper.sendMsg(Urls.qqSendGroupMsg,QQHelper.data.replace("<group>",this.group).replace("<file>",convertPathToUri(md5)));
            //QQHelper.sendGroupMsg(this.group,QQData.Msg.create(QQData.MsgType.Image, "file",convertPath(md5)));
        }else {
            sendMsg(r);
        }
        return true;
    }
    public static String convertPath(String inputPath) {
        if (inputPath == null || inputPath.isEmpty()) {
            return inputPath;
        }

        // 1. 确保使用 "\" 作为分隔符 (先替换双反斜杠为单反斜杠)
        String replacedSlashes = inputPath.replace("/", "\\").replace("\\\\", "\\");

        // 2. 处理盘符后的反斜杠
        StringBuilder sb = new StringBuilder(replacedSlashes);
        if (sb.length() >= 2 && sb.charAt(1) == ':') {
            // 确保盘符后有两个反斜杠
            if (sb.length() >= 3) {
                if (sb.charAt(2) != '\\') {
                    sb.insert(2, '\\');
                }
            } else {
                sb.append("\\");
            }

            if (sb.length() >= 4) {
                if (sb.charAt(3) != '\\') {
                    sb.insert(3, '\\');
                }
            } else {
                sb.append("\\");
            }
        }

        // 3. 构建 file:/// URI
        String fileUri = "file:///" + sb.toString();

        return fileUri;
    }
    public static String convertPathToUri(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return filePath; // 或者抛出异常
        }

        // 1. 替换反斜杠为正斜杠
        String unixPath = filePath.replace("\\", "/");

        // 2. 处理盘符 (如果存在)
        if (unixPath.length() >= 2 && unixPath.charAt(1) == ':') {
            unixPath = unixPath.substring(0, 2) + "//" + unixPath.substring(2);
        }

        // 3. 添加 file:/// 前缀
        return "file:///" + unixPath;
    }
    private boolean findItem() {
        if (CommandConfig.I.isEnable("findItem")) return false;
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：物品信息 物品名称");
            return false;
        }
        if (args[1].isEmpty()){
            sendMsg("物品名称不能为空");
            return false;
        }
        _Log.info("查找物品");
        sendMsg(GameInfo.I.findItem(args[1]));
        return true;
    }

    private boolean getItemInfo() {
        if (CommandConfig.I.isEnable("getItemInfo")) return false;
        if (args[1].isEmpty()){
            sendMsg("物品名称不能为空");
            return false;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (s.isEmpty()) s.append(args[i]);
            else s.append(" ").append(args[i]);
        }
        _Log.info("获取物品信息");

        String r = GameInfo.I.getItemInfo(s.toString());

        if (Config.I.getDatas().imageItem){
            String md5 = DataTable.ImageCache+"/"+_Byte.getMD5(r)+".png";
            if (!new File(md5).exists()) DrawConfig.I.createImage(List.of(r.split("\\\\n"))).save(md5);
            QQHelper.sendMsg(Urls.qqSendGroupMsg,QQHelper.data.replace("<group>",this.group).replace("<file>",convertPathToUri(md5)));
        }else {
            sendMsg(r);
        }
        return true;
    }

    private boolean findWhite() {
        if (CommandConfig.I.isEnable("findWhite")) return false;
        if (rawArg.isEmpty()){
            sendMsg("物品名称不能为空");
            return false;
        }
        _Log.info("查找白名单");
        StringBuilder s = new StringBuilder();
        int[] c = {0};
        ACItemsConfig.I.getDatas().forEach((k, v) -> {
            for(String item : v.items()){
                /*
                if (item.contains(this.args[1])) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }

                 */
                boolean full = true;
                for (int i = 1; i < this.args.length; i++) {
                    if (!item.contains(this.args[i])) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }
            }
        });
        AutoWhiteList.I.getDatas().forEach((k, v) -> {
                for(String item : v.items()){
                    boolean full = true;
                    for (int i = 1; i < this.args.length; i++) {
                        if (!item.contains(this.args[i])) {
                            full = false;
                            break;
                        }
                    }
                    if (full) {
                        c[0]++;
                        s.append("\\n").append(k);
                        break;
                    }
                }
            });

        if (c[0] > 20) {
            sendMsg("结果数量过多，请提供更多字词并重新查找");
            return true;
        }
        if (s.isEmpty()) sendMsg("未找到包含此物品的白名单，请检查是否有错误。");
        else sendMsg("包含类似物品的白名单："+ s);
        return true;
    }

    private boolean receiveGift() {
        if (CommandConfig.I.isEnable("receiveGift")) return false;
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：领取礼包 礼包名称");
            return false;
        }
        _Log.info("领取礼包");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            String r = args[1];
            _Log.debug("已绑定账号");
            if (config.isReward(r))
                sendMsg("您已领取过【" + r + "】礼包");
            else {
                sendMsg( BotNet.giveReward(config, r));
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg("未绑定账号，请先绑定账号");
        }
        return true;
    }

    private boolean requestInfo() {
        if (CommandConfig.I.isEnable("requestInfo")) return false;
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：查信息 玩家名称");
            return false;
        }
        _Log.info("查看玩家信息");
        BotData.PlayerInfo info = BotNet.getOnlinePlayerByName(args[1]);
        if (info == null) {
            _Log.debug("未找到玩家");
            sendMsg("未找到玩家，请确认玩家是否在线");
        } else {
            sendMsg(info.toString());
        }
        return true;
    }

    private boolean applyWhite() {
        if (CommandConfig.I.isEnable("applyWhite")) return false;
        if (this.rawArg.isEmpty()) {
            sendMsg("指令格式错误，正确格式：申请白名单 白名单名称");
            return false;
        }
        _Log.info("申请白名单");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            _Log.debug("已绑定账号");
            BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                _Log.debug("未找到玩家");
                sendMsg( "未找到玩家，请确认玩家是否在线");
            } else {
                ACItemsData data = ACItemsConfig.I.get(rawArg);
                if (data == null) data = AutoWhiteList.I.getDatas().get(rawArg);
                if (data == null) {
                    _Log.debug("未找到此白名单");
                    sendMsg( "未找到此白名单，请确认此白名单是否存在");
                } else {
                    if (data.allNeed() ? info.point() >= data.point() && info.level() >= data.level() : info.point() >= data.point() || info.level() >= data.level()) {
                        _Log.debug("白名单检测成功");
                        if (ACNet.I.addWhite(info.userid(), data.getFormatItems())) {
                            sendMsg( "白名单添加成功");
                        } else {
                            sendMsg( "白名单添加失败，网络异常");
                        }
                    } else {
                        sendMsg( "白名单添加失败，你未满足白名单要求：" + "\\n需要等级：" + data.level() + "\\n需要积分：" + data.point() + "\\n" + (data.allNeed() ? "需要等级和积分全部满足" : "需要等级或积分任一满足"));

                    }

                }
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }

    private boolean requestWhite() {
        if (CommandConfig.I.isEnable("requestWhite")) return false;
        if (rawArg.isEmpty()) {
            sendMsg("指令格式错误，正确格式：查询白名单 白名单名称");
            return false;
        }
        _Log.info("查询白名单");
        ACItemsData data = ACItemsConfig.I.get(rawArg);
        if (data == null) data = AutoWhiteList.I.getDatas().get(rawArg);
        if (data == null) {
            _Log.debug("未找到此白名单");
            sendMsg( "未找到此白名单，请确认此白名单是否存在");
        } else {
            _Log.debug("白名单查询成功");
            sendMsg( data.toString());
        }
        return true;
    }

    private boolean agreeUnban() {
        if (CommandConfig.I.isEnable("agreeUnban")) return false;
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：同意解封 qq");
            return false;
        }
        _Log.info("同意解封");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            _Log.debug("已绑定账号");

            if (QQUsualCommand.banUser.containsKey(args[1])) {
                if (args[1].equals(this.qq)) {
                    _Log.debug("相同玩家");
                    sendMsg( "您不能同意自己的解封申请");
                    return true;
                }
                List<String> count = QQUsualCommand.banUser.get(args[1]);
                if (count.contains(this.qq)) {
                    _Log.debug("已同意过此玩家的解封申请");
                    sendMsg( "您已同意过此玩家的解封申请");
                    return true;
                }
                count.add(this.qq);
                QQUsualCommand.banUser.put(args[1], count);
                if (count.size() >= Config.I.getDatas().unBanNum) {
                    QQUsualCommand.banUser.remove(args[1]);
                    sendMsg( "已满足解封所需人数，即将解除封禁");
                    UserConfig b = new UserConfig(args[1]);
                    if (KitNet.unBan(b.getSteamID())) {
                        QQHelper.easySendGroupAtMsg(message.group_id, args[1], "解封成功");
                    } else {
                        QQHelper.easySendGroupAtMsg(message.group_id, args[1], "解封失败，网络异常");
                    }
                } else {
                    _Log.debug("未满足解封所需人数");
                    sendMsg( "您已同意此玩家的解封申请");
                }
            } else {
                _Log.debug("未找到此玩家");
                sendMsg( "未找到此封禁玩家");
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }

    private boolean buySaveItemNum() {
        if (CommandConfig.I.isEnable("buySaveItemNum")) return false;
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：购买跟档物品数量 数量");
            return false;
        }
        _Log.info("提高跟档物品数量");
        UserConfig config = new UserConfig(this.qq);
        if (config.isBind()) {
            _Log.debug("已绑定账号");
            if (config.getRecordItemLimit() >= Config.I.getDatas().recordItemLimit){
                _Log.debug("达到上限");
                sendMsg( "您的跟档物品数量已达到上限");
                return true;
            }
            BotData.PlayerInfo info = BotNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                sendMsg("请在线后再试");
                return true;
            }
            int n = Integer.parseInt(args[1]);
            if (config.getRecordItemLimit() + n >= Config.I.getDatas().recordItemLimit) n = Config.I.getDatas().recordItemLimit - config.getRecordItemLimit();
            int p  = n * Config.I.getDatas().recordItemPoint;
            if (p <= 0) {
                sendMsg("数据错误，请重新尝试");
                return true;
            }
            if (info.point() < p){
                sendMsg("您的积分不足");
                return true;
            }
            if(BotNet.sendPoint(config.getSteamID(), -p)){
                config.getDatas().recordItemLimit = config.getRecordItemLimit() + n;
                config.save();
                sendMsg( "已增加"+n+"个跟档物品数量，当前数量"+config.getRecordItemLimit());
            }else sendMsg("添加失败，请重新尝试");
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }

    public boolean argCheck(int length) {
        return this.args.length != length;
    }
}
