package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = KillSelfCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class KillSelfCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "自杀";
    public KillSelfCommand( QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return killSelf();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean killSelf() {
        $7DTD._Log.info("自杀");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) sendMsg("usual.command.error.not_online");
            else {
                if (server.sendServerCommand("kill "+info.entityid())) sendMsg("kill_self.command.success");
                else sendMsg("kill_self.command.error.net");
            }
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }
}
