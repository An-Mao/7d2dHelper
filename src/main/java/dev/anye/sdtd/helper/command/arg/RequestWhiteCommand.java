package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.data.ACItemsData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = RequestWhiteCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查询白名单 白名单名称")
public class RequestWhiteCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查询白名单";
    public RequestWhiteCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return requestWhite();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean requestWhite() {
        if (rawArg.isEmpty()) {
            sendMsg("request_white.command.error.empty");
            return false;
        }
        $7DTD._Log.info("查询白名单");
        ACItemsData data = server.acItem.get(rawArg);
        if (data == null) data = server.autoWhiteList.getDatas().get(rawArg);
        if (data == null) sendMsg( "request_white.command.error.not_found");
        else sendMsg( data.toString());
        return true;
    }

}
