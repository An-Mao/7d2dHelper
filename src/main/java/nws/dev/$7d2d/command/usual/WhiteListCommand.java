package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "白名单列表",permission = Permission.User,type = CommandType.Group)
public class WhiteListCommand extends QQUsualCommand {
    public WhiteListCommand(QQData.Message message, ServerCore serverCore) {
        super("whiteList", message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return whiteList();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean whiteList() {
        _Log.info("获取白名单列表");
        StringBuilder s = new StringBuilder("-----白名单列表-----");
        server.acItem.getDatas().forEach((k, v) -> s.append("\\n").append(k));
        sendMsg(s.toString());
        return true;
    }
}
