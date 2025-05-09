package nws.dev.$7d2d.command.at;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.config.SingInConfig;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

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
                    $7DTD._Log.debug(s);
                    sendMsg(s);
                } else sendMsg("对方未绑定账号");
            } else sendMsg("未绑定账号，请先绑定账号");

        return true;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
