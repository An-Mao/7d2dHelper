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
@Command(name = SendSaveItemCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.All)
public class SendSaveItemCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "提取物品";
    public SendSaveItemCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
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
            sendMsg("usual.command.error.not_bind");
            return true;
        }
        if (!config.getDatas().canExtractSaveItem){
            sendMsg("send_save_item.command.error.need_check");
            return true;
        }
        UserConfig.RecordItem recordItem = config.getRecordItem(server);
        if (recordItem.getDatas().isEmpty()) {
            sendMsg("send_save_item.command.error.not_item");
            return true;
        }
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info == null) {
            sendMsg("usual.command.error.not_online");
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
        sendFormatMsg("send_save_item.command.success",success.size());
        return true;
    }
}
