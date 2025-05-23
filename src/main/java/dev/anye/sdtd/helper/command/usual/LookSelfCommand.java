package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = LookSelfCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class LookSelfCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "查自己";
    public LookSelfCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return lookSelf();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean lookSelf() {
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) sendMsg( "usual.command.error.not_online");
            else sendMsg( info.toString());
        }else sendMsg( "usual.command.error.not_bind");
        return true;
    }
}
