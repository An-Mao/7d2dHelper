package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = "清理服务器",permission = Permission.ServerAdmin,type = CommandType.Private)
public class ClearServerCommand extends QQUsualCommand {
    public ClearServerCommand(QQData.Message message, ServerCore serverCore) {
        super("clearServer", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        sendMsg("即将清理服务器");
        server.restart();
        return true;
    }
}
