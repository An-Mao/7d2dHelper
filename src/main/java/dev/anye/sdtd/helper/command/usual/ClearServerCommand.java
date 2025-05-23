package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = ClearServerCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Private)
public class ClearServerCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "清理服务器";
    public ClearServerCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        sendMsg("clear_server.command.start");
        server.restart();
        return true;
    }
}
