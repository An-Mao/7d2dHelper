package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = ReloadConfigCommand.COMMAND_NAME,permission = Permission.Admin,type = CommandType.Private)
public class ReloadConfigCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "重新加载配置";

    public ReloadConfigCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        sendMsg("reload_config.command.start");
        server.reloadConfig();
        return true;
    }
}
