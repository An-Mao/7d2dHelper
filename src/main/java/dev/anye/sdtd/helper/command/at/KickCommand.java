package dev.anye.sdtd.helper.command.at;

import dev.anye.sdtd.helper.command.QQAtCommand;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

//@Command(name = UnbanCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Group,desc = "踢@QQ")
public class KickCommand extends QQAtCommand {
    public static final String COMMAND_NAME = "踢";
    public KickCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
