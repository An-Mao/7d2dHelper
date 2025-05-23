package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = ServerStateCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class ServerStateCommand extends ServerInfoCommand{
    public static final String COMMAND_NAME = "服务器状态";
    public ServerStateCommand(QQData.Message message, ServerCore serverCore) {
        super(message,serverCore);
    }
}
