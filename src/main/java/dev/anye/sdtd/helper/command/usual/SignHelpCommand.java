package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = SignHelpCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class SignHelpCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "签到帮助";
    public SignHelpCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return signHelp();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean signHelp() {
        $7DTD._Log.info("获取签到帮助");
        sendMsg("sign_help.command.tip");
        return true;
    }
}
