package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = "同意跟档",permission = Permission.ServerAdmin,type = CommandType.Private,desc = "同意跟档 qq")
public class AgreeSaveCommand extends QQExCommand {
    public AgreeSaveCommand(QQData.Message message, ServerCore serverCore) {
        super("agreeSave", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        return agreeSave();
    }

    private boolean agreeSave() {
            String target = args[1];
            if (target.isEmpty()) {
                sendMsg("指令格式错误，正确格式：同意跟档 qq");
                return false;
            }else {
                UserConfig config = server.getUserData(target);
                if (config.isBind()) {
                    config.getDatas().canExtractSaveItem = true;
                    config.save();
                    sendMsg("已同意跟档申请");
                } else sendMsg("对方未绑定账号");
            }

        return true;
    }
}
