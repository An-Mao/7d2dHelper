package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = RequestInfoCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查信息 玩家名称")
public class RequestInfoCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查信息";
    public RequestInfoCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return requestInfo();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
    private boolean requestInfo() {
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：查信息 玩家名称");
            return false;
        }
        $7DTD._Log.info("查看玩家信息");
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerByName(args[1]);
        if (info == null) {
            $7DTD._Log.debug("未找到玩家");
            sendMsg("未找到玩家，请确认玩家是否在线");
        } else {
            sendMsg(info.toString());
        }
        return true;
    }
}
