package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.SingInConfig;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = SignCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class SignCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "签到";
    public SignCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return sign();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean sign() {
        $7DTD._Log.info("签到");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            SingInConfig singInConfig = server.getSignData(this.qq);
            String s = singInConfig.sign(server,config.getSteamID());
            $7DTD._Log.debug(s);
            sendMsg(s);
        } else {
            $7DTD._Log.debug("未绑定账号");
            sendMsg("未绑定账号，请先绑定账号");
        }
        return true;

    }
}
