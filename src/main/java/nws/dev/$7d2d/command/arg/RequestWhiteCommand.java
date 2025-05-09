package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.data.ACItemsData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = RequestWhiteCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查询白名单 白名单名称")
public class RequestWhiteCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查询白名单";
    public RequestWhiteCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
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
        $7DTD._Log.info("查询白名单");
        ACItemsData data = server.acItem.get(rawArg);
        if (data == null) data = server.autoWhiteList.getDatas().get(rawArg);
        if (data == null) {
            $7DTD._Log.debug("未找到此白名单");
            sendMsg( "未找到此白名单，请确认此白名单是否存在");
        } else {
            $7DTD._Log.debug("白名单查询成功");
            sendMsg( data.toString());
        }
        return true;
    }

}
