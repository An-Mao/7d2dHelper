package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = AgreeSaveCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Private,desc = "同意跟档 qq")
public class AgreeSaveCommand extends QQExCommand {
    public static final String COMMAND_NAME = "同意跟档";
    public AgreeSaveCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        return agreeSave();
    }

    private boolean agreeSave() {
            String target = args[1];
            if (target.isEmpty()) {
                sendMsg("agree_save.command.error.args_number");
                return false;
            }else {
                UserConfig config = server.getUserData(target);
                if (config.isBind()) {
                    config.getDatas().canExtractSaveItem = true;
                    config.save();
                    sendMsg("agree_save.command.success");
                } else sendMsg("usual.command.error.target_not_bind");
            }

        return true;
    }
}
