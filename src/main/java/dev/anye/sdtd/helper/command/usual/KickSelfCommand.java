package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = KickSelfCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class KickSelfCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "踢自己";
    public KickSelfCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return kickSelf();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    public boolean kickSelf() {
        UserConfig config = server.getUserData(this.qq);
        if (!config.isBind()) {
            sendMsg("usual.command.error.not_bind");
            return true;
        }
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info == null) sendMsg("usual.command.error.not_online");
        else {
            if (server.kitNet.kick(config.getSteamID())) sendMsg("kick_self.command.success");
            else sendMsg("kick_self.command.error.net");
        }
        return true;

    }
}
