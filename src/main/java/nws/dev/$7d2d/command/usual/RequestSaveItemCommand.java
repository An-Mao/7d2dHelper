package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.*;
import nws.dev.$7d2d.server.ServerCore;

import java.util.List;

@Command(name = "申请跟档",permission = Permission.User,type = CommandType.All)
public class RequestSaveItemCommand extends QQUsualCommand {
    public RequestSaveItemCommand(QQData.Message message, ServerCore serverCore) {
        super("requestSaveItem", message,serverCore);
    }

    @Override
    public boolean privateMsg() {
        return requestSaveItem();
    }

    @Override
    public boolean groupMsg() {
        return requestSaveItem();
    }

    public boolean requestSaveItem() {
        UserConfig config = server.getUserData(this.qq);
        if (!config.isBind()) {
            sendMsg("未绑定账号，请先绑定账号");
            return true;
        }



        UserConfig.RecordItem recordItem = config.getRecordItem(server);
        if (!recordItem.getDatas().isEmpty()) {
            sendMsg("您已申请过跟档，如果想要修改，请先提取物品。");
            return true;
        }
        KitData.GsList list = server.kitNet.getGsList();
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
        int recordItemLimit = server.serverData.recordItemDefault() + config.getRecordItemLimit();
        if (System.currentTimeMillis() - server.saveItem.getOrDefault(this.qq, 0L) > 30000) {
            server.saveItem.put(this.qq, System.currentTimeMillis());
            sendMsg("请确认是否已将物品上的模组卸下，您可以跟档" + recordItemLimit + "个物品，如果确认无误请再次发送此指令。");
            return true;
        }
        server.saveItem.remove(this.qq);
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info != null) {
            sendMsg("请离线后再试");
            return true;
        }
        PlayerInfoData playerInfoData = server.kitNet.getBagItems(server.kitNet.formatSteamId(config.getSteamID()));
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
            if (server.kitNet.removeBagItems(server.kitNet.formatSteamId(config.getSteamID()), String.valueOf(itemData.id()))) {
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
