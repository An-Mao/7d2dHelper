package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.config.UserUaualConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;
import dev.anye.sdtd.helper.server.ServerList;

@Command(name = BindServerCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Private,desc = "绑定服务器 服务器名")
public class BindServerCommand extends QQExCommand {
    public static final String COMMAND_NAME = "绑定服务器";
    public BindServerCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean runCommand() {
            if (isGroup && isEnableGroup())return false;
            if (this.rawArg.isEmpty() || !ServerList.LIST.containsKey(this.rawArg)) {
                sendMsg("bind_server.command.error.not_found");
                return true;
            }
            UserUaualConfig userUaualConfig = new UserUaualConfig(this.qq);
            userUaualConfig.getDatas().setPrivateServer(this.rawArg);
            userUaualConfig.save();
            sendMsg("bind_server.command.success");
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
