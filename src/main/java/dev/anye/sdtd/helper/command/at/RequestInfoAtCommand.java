package dev.anye.sdtd.helper.command.at;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQAtCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.KitData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = RequestInfoAtCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查详细信息@QQ",priority = 990)
public class RequestInfoAtCommand extends QQAtCommand {
    public static final String COMMAND_NAME = "查详细信息";
    public RequestInfoAtCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        if (this.target.isEmpty()) return false;
        UserConfig config = server.getUserData(this.target);
        if (!config.isBind()) sendMsg("usual.command.error.not_bind");
        else {

            BotData.UserInfo playerInfo = server.botNet.getUserBySteamID(config.getSteamID());
            if (playerInfo == null) sendMsg("usual.command.error.net");
            else {
                UserConfig t = server.getUserData(this.target);
                if (!t.isBind()) sendMsg("usual.command.error.target_not_bind");
                else {
                    int point = server.commandPoint.getPoint(this.c);
                    if (playerInfo.point() < point)
                        sendMsg("usual.command.error.point_insufficient");
                    else {
                        if (server.botNet.sendPoint(config.getSteamID(), -point)) {
                            KitData.Player player = server.kitNet.getPlayerWithSteamId(t.getSteamID());
                            if (player == null){
                                sendMsg("usual.command.error.net");
                            }else {
                                sendFormatMsg("request_info.command.success.plus",player.name(),player.getCreateTime(),player.getLoginTime(),player.level(),player.health(),player.skillpoint(),player.getStatus());
                            }
                        } else sendMsg("usual.command.error.point.sub");

                    }
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
