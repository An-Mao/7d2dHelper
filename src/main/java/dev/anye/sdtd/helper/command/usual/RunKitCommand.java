package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = RunKitCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Private)
public class RunKitCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "运行kit";
    public RunKitCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        if (System.currentTimeMillis() - server.wt < 60000) {
            if (server.restartThread.isAlive()) sendMsg("run_kit.command.error.running");
            else {
                sendMsg("run_kit.command.start");
                server.runKitExe();
            }
        } else {
            sendMsg("run_kit.command.step.1");
            server.wt = System.currentTimeMillis();
        }
        return true;
    }
}
