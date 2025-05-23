package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = StartServerCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Private)
public class StartServerCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "运行kit";
    public StartServerCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        if (System.currentTimeMillis() - server.wt < 60000) {
            if (server.restartThread.isAlive()) sendMsg("start_server.command.error.running");
            else {
                sendMsg("start_server.command.start");
                server.kitNet.startServer();
            }
        } else {
            sendMsg("start_server.command.step.1");
            server.wt = System.currentTimeMillis();
        }
        return true;
    }
}
