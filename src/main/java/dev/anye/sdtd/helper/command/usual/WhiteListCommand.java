package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = WhiteListCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class WhiteListCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "白名单列表";
    public WhiteListCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return whiteList();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean whiteList() {
        $7DTD._Log.info("获取白名单列表");
        StringBuilder s = new StringBuilder("-----白名单列表-----");
        server.acItem.getDatas().forEach((k, v) -> s.append("\\n").append(k));
        sendMsg(s.toString());
        return true;
    }
}
