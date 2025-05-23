package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = BuySaveItemNumCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "购买跟档物品数量 数量")
public class BuySaveItemNumCommand extends QQExCommand {
    public static final String COMMAND_NAME = "购买跟档物品数量";
    public BuySaveItemNumCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return buySaveItemNum();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean buySaveItemNum() {
        if (argCheck(2)) {
            sendMsg("buy_save_item_num.command.error.args_number");
            return false;
        }
        $7DTD._Log.info("提高跟档物品数量");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            if (config.getRecordItemLimit() >= server.serverData.recordItemLimit()){
                sendMsg("buy_save_item_num.command.error.limit");
                return true;
            }
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                sendMsg("usual.command.error.not_online");
                return true;
            }
            int n = Integer.parseInt(args[1]);
            if (config.getRecordItemLimit() + n >= server.serverData.recordItemLimit()) n = server.serverData.recordItemLimit() - config.getRecordItemLimit();
            int p  = n * server.serverData.recordItemPoint();
            if (p <= 0) {
                sendMsg("buy_save_item_num.command.error.data");
                return true;
            }
            if (info.point() < p){
                sendMsg("usual.command.error.point_insufficient");
                return true;
            }
            if(server.botNet.sendPoint(config.getSteamID(), -p)){
                config.getDatas().recordItemLimit = config.getRecordItemLimit() + n;
                config.save();
                sendFormatMsg("buy_save_item_num.command.success",n,config.getRecordItemLimit());
            }else sendMsg("buy_save_item_num.command.error.net");
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }
}
