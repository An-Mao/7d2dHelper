package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "查找物品",permission = Permission.User,type = CommandType.Group,desc = "查找物品 物品名")
public class FindItemCommand extends QQExCommand {
    public FindItemCommand(QQData.Message message, ServerCore serverCore) {
        super("findItem", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return findItem();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean findItem() {
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：物品信息 物品名称");
            return false;
        }
        if (args[1].isEmpty()){
            sendMsg("物品名称不能为空");
            return false;
        }
        _Log.info("查找物品");
        sendMsg(server.gameInfo.findItem(args[1]));
        return true;
    }

}
