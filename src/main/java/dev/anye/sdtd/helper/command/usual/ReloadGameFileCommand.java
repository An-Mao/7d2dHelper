package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = ReloadGameFileCommand.COMMAND_NAME,permission = Permission.Admin,type = CommandType.Private)
public class ReloadGameFileCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "重新读取游戏文件";
    public ReloadGameFileCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        sendMsg("reload_game_file.command.start");
        server.gameInfo.init();
        sendMsg("reload_game_file.command.done");
        return true;
    }
}
