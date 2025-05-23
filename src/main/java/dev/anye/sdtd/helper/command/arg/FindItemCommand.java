package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = FindItemCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查找物品 物品名")
public class FindItemCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查找物品";
    public FindItemCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return findItem();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean findItem() {
        if (argCheck(2)) {
            sendMsg("find_item.command.error.args_number");
            return false;
        }
        if (args[1].isEmpty()){
            sendMsg("find_item.command.error.empty");
            return false;
        }
        $7DTD._Log.info("查找物品");
        sendMsg(server.gameInfo.findItem(args[1]));
        return true;
    }

}
