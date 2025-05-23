package dev.anye.sdtd.helper.command.at;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQAtCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = UnbanCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Group,desc = "解封@QQ")
public class UnbanCommand extends QQAtCommand {
    public static final String COMMAND_NAME = "解封";
    public UnbanCommand( QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        //$7DTD._Log.info("解封" + this.target);
        server.banUser.remove(this.target);
        UserConfig b = server.getUserData(this.target);
        if (server.kitNet.unBan(b.getSteamID())) sendMsg("unban.command.success");
        else sendMsg("unban.command.error.net");
        return false;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
