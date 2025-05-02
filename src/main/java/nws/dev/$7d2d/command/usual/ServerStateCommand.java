package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = "服务器状态",permission = Permission.User,type = CommandType.Group)
public class ServerStateCommand extends ServerInfoCommand{
    public ServerStateCommand(QQData.Message message, ServerCore serverCore) {
        super(message,serverCore);
    }
}
