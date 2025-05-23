package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = RequestInfoCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查信息 玩家名称")
public class RequestInfoCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查信息";
    public RequestInfoCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return requestInfo();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
    private boolean requestInfo() {
        if (argCheck(2)) {
            sendMsg("request_info.command.error.args_number");
            return false;
        }
        $7DTD._Log.info("查看玩家信息");
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerByName(args[1]);
        if (info == null) sendMsg("usual.command.error.target_not_online");
        else sendMsg(info.toString());
        return true;
    }
}
