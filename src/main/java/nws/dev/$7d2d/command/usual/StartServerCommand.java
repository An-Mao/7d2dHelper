package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

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
