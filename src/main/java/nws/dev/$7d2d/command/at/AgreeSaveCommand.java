package nws.dev.$7d2d.command.at;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = AgreeSaveCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Group,desc = "同意跟档@QQ",priority = 990)
public class AgreeSaveCommand extends QQAtCommand {
    public static final String COMMAND_NAME = "同意跟档";
    public AgreeSaveCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        if (target.isEmpty()) {
            //sendMsg("指令格式错误，正确格式：同意跟档 @qq");
            return false;
        } else {
            UserConfig config = server.getUserData(this.target);
            if (config.isBind()) {
                config.getDatas().canExtractSaveItem = true;
                config.save();
                sendMsg("agree_save.command.success");
            } else sendMsg("usual.command.error.target_not_bind");
        }
        return true;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
