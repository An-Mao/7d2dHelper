package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.*;
import nws.dev.$7d2d.server.ServerCore;

import java.util.List;

@Command(name = RequestSaveItemCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.All)
public class RequestSaveItemCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "申请跟档";
    public RequestSaveItemCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
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
            sendMsg("usual.command.error.not_bind");
            return true;
        }



        UserConfig.RecordItem recordItem = config.getRecordItem(server);
        if (!recordItem.getDatas().isEmpty()) {
            sendMsg("request_save_item.command.error.has");
            return true;
        }
        KitData.GsList list = server.kitNet.getGsList();
        if (list.result() == 1) {
            for (KitData.GsInfo info : list.list()) {
                if (info.status() != 1) {
                    sendMsg("request_save_item.command.error.server");
                    return true;
                }
            }
        }else {
            sendMsg("request_save_item.command.error.net");
            return true;
        }
        int recordItemLimit = server.serverData.recordItemDefault() + config.getRecordItemLimit();
        if (System.currentTimeMillis() - server.saveItem.getOrDefault(this.qq, 0L) > 30000) {
            server.saveItem.put(this.qq, System.currentTimeMillis());
            sendFormatMsg("request_save_item.command.step.1", recordItemLimit);
            return true;
        }
        server.saveItem.remove(this.qq);
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info != null) {
            sendMsg("request_save_item.command.error.online");
            return true;
        }
        PlayerInfoData playerInfoData = server.kitNet.getBagItems(server.kitNet.formatSteamId(config.getSteamID()));
        if (playerInfoData == null) {
            sendMsg("request_save_item.command.error.bag");
            return true;
        }
        if (playerInfoData.bag().size() > recordItemLimit) {
            sendMsg("request_save_item.command.error.bag.more");
            return true;
        }
        if (playerInfoData.bag().isEmpty()) {
            sendMsg("request_save_item.command.error.bag.empty");
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
        sendFormatMsg("request_save_item.command.success", success , (items.size() - success));
        return true;
    }
}
