package nws.dev.$7d2d.command.at;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = "同意跟档+",permission = Permission.ServerAdmin,type = CommandType.Group)
public class AgreeSaveCommand extends QQAtCommand {
    public AgreeSaveCommand(QQData.Message message, ServerCore serverCore) {
        super("agreeSave", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        if (target.isEmpty()) {
            sendMsg("指令格式错误，正确格式：同意跟档 @qq");
            return false;
        } else {
            UserConfig config = server.getUserData(this.target);
            if (config.isBind()) {
                config.getDatas().canExtractSaveItem = true;
                config.save();
                sendMsg("已同意跟档申请");
            } else sendMsg("对方未绑定账号");
        }

        return true;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
