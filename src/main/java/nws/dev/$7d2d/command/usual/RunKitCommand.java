package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

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
