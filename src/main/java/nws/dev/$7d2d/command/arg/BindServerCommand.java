package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserUaualConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = BindServerCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Private,desc = "绑定服务器 服务器名")
public class BindServerCommand extends QQExCommand {
    public static final String COMMAND_NAME = "绑定服务器";
    public BindServerCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean runCommand() {
            if (isGroup && isEnableGroup())return false;
            if (this.rawArg.isEmpty() || !ServerCore.LIST.containsKey(this.rawArg)) {
                sendMsg("指定服务器名称不存在，请重试");
                return true;
            }
            UserUaualConfig userUaualConfig = new UserUaualConfig(this.qq);
            userUaualConfig.getDatas().setPrivateServer(this.rawArg);
            userUaualConfig.save();
            sendMsg("绑定成功");
            return true;
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
