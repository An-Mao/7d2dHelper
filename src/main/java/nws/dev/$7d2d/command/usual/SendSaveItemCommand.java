package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.PlayerInfoData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

import java.util.ArrayList;
import java.util.List;
@Command(name = "提取物品",permission = Permission.User,type = CommandType.All)
public class SendSaveItemCommand extends QQUsualCommand {
    public SendSaveItemCommand(QQData.Message message, ServerCore serverCore) {
        super("sendSaveItem", message,serverCore);
    }

    @Override
    public boolean privateMsg() {
        return sendSaveItem();
    }

    @Override
    public boolean groupMsg() {
        return sendSaveItem();
    }

    public boolean sendSaveItem() {
        UserConfig config = server.getUserData(this.qq);
        if (!config.isBind()) {
            sendMsg("未绑定账号，请先绑定账号");
            return true;
        }
        if (!config.getDatas().canExtractSaveItem){
            sendMsg("无法提取物品，请等待服主核验。");
            return true;
        }
        UserConfig.RecordItem recordItem = config.getRecordItem(server);
        if (recordItem.getDatas().isEmpty()) {
            sendMsg("没有需要提取的物品");
            return true;
        }
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info == null) {
            sendMsg("请在线后再试");
            return true;
        }
        List<PlayerInfoData.ItemData> success = new ArrayList<>();
        recordItem.getDatas().forEach((itemData) -> {
            if (server.botNet.giveItem(config.getSteamID(), itemData.n(), itemData.c(), itemData.q())){
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
}
