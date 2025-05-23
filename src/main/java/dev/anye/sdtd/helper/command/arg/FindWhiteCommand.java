package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = FindWhiteCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查找白名单 物品名称")
public class FindWhiteCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查找白名单";
    public FindWhiteCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return findWhite();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean findWhite() {
        if (rawArg.isEmpty()){
            sendMsg("find_item.command.error.empty");
            return false;
        }
        $7DTD._Log.info("查找白名单");
        StringBuilder s = new StringBuilder();
        int[] c = {0};
        server.acItem.getDatas().forEach((k, v) -> {
            for(String item : v.items()){
                /*
                if (item.contains(this.args[1])) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }

                 */
                boolean full = true;
                for (int i = 1; i < this.args.length; i++) {
                    if (!item.contains(this.args[i])) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }
            }
        });
        server.autoWhiteList.getDatas().forEach((k, v) -> {
            for(String item : v.items()){
                boolean full = true;
                for (int i = 1; i < this.args.length; i++) {
                    if (!item.contains(this.args[i])) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }
            }
        });

        if (c[0] > 20) {
            sendMsg("find_white.command.error.too_more");
            return true;
        }
        if (s.isEmpty()) sendMsg("find_white.command.error.not_found");
        else sendFormatMsg("find_white.command.success", s.toString());
        return true;
    }
}
