package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

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
