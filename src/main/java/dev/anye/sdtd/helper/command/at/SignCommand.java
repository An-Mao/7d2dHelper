package dev.anye.sdtd.helper.command.at;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQAtCommand;
import dev.anye.sdtd.helper.config.SingInConfig;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = SignCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "签到@QQ",priority = 990)
public class SignCommand extends QQAtCommand {
    public static final String COMMAND_NAME = "签到";
    public SignCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
            if (target.isEmpty()) {
                //sendMsg("指令格式错误，正确格式：签到 @qq");
                return false;
            }
            UserConfig config = server.getUserData(this.qq);
            if (config.isBind()) {
                UserConfig c = server.getUserData(target);
                if (c.isBind()) {
                    SingInConfig singInConfig = server.getSignData(this.qq);
                    String s = singInConfig.sign(server,config.getSteamID(), c.getSteamID());
                    sendMsg(s);
                } else sendMsg("usual.command.error.target_not_bind");
            } else sendMsg("usual.command.error.not_bind");

        return true;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
