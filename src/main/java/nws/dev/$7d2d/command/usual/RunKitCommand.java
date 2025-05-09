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
            if (server.restartThread.isAlive()) sendMsg("重启进程运行中，请勿执行此指令");
            else {
                sendMsg("即将运行kit");
                server.runKitExe();
            }
        } else {
            sendMsg("您正在运行高危指令，如果确实想运行，请在60秒内再发一次此指令");
            server.wt = System.currentTimeMillis();
        }
        return true;
    }
}
