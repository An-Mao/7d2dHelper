package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.SingInConfig;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

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
        } else sendMsg("usual.command.error.not_bind");
        return true;

    }
}
