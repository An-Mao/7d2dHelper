package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = "重新加载配置",permission = Permission.Admin,type = CommandType.Private)
public class ReloadConfigCommand extends QQUsualCommand {

    public ReloadConfigCommand(QQData.Message message, ServerCore serverCore) {
        super("reloadConfig", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        sendMsg("即将重新加载配置");
        server.reloadConfig();
        return true;
    }
}
