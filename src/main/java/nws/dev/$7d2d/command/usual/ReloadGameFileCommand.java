package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = "重新读取游戏文件",permission = Permission.Admin,type = CommandType.Private)
public class ReloadGameFileCommand extends QQUsualCommand {
    public ReloadGameFileCommand(QQData.Message message, ServerCore serverCore) {
        super("reloadGameFileCommand", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        sendMsg("开始重新读取游戏文件");
        server.gameInfo.init();
        sendMsg("重新读取游戏文件完成");
        return true;
    }
}
