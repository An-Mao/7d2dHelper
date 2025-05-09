package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

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
        sendMsg("签到时您可以@一位玩家来绑定。如果对方当日签到并且您在线，则可以额外获得一次签到奖励。若其当日未签到，您将无法获取下次签到奖励");
        return true;
    }
}
