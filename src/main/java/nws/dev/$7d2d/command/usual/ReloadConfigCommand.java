package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = ReloadConfigCommand.COMMAND_NAME,permission = Permission.Admin,type = CommandType.Private)
public class ReloadConfigCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "重新加载配置";

    public ReloadConfigCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        sendMsg("reload_config.command.start");
        server.reloadConfig();
        return true;
    }
}
