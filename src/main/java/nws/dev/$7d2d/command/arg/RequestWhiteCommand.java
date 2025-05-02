package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.data.ACItemsData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "查询白名单",permission = Permission.User,type = CommandType.Group)
public class RequestWhiteCommand extends QQExCommand {
    public RequestWhiteCommand(QQData.Message message, ServerCore serverCore) {
        super("requestWhite", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return requestWhite();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean requestWhite() {
        if (rawArg.isEmpty()) {
            sendMsg("指令格式错误，正确格式：查询白名单 白名单名称");
            return false;
        }
        _Log.info("查询白名单");
        ACItemsData data = server.acItem.get(rawArg);
        if (data == null) data = server.autoWhiteList.getDatas().get(rawArg);
        if (data == null) {
            _Log.debug("未找到此白名单");
            sendMsg( "未找到此白名单，请确认此白名单是否存在");
        } else {
            _Log.debug("白名单查询成功");
            sendMsg( data.toString());
        }
        return true;
    }

}
