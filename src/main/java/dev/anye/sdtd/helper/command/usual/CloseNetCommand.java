package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = CloseNetCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Private)
public class CloseNetCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "关闭网关";
    public CloseNetCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        if (System.currentTimeMillis() - server.wt < 60000) {
            if (server.restartThread.isAlive()) sendMsg("close_net.command.error.is_restart");
            else {
                sendMsg("close_net.command.close");
                server.kitNet.stopNet();
            }
        } else {
            sendMsg("close_net.command.warn");
            server.wt = System.currentTimeMillis();
        }
        return true;
    }
}
