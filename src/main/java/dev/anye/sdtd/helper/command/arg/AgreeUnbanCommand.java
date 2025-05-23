package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

import java.util.List;

@Command(name = AgreeUnbanCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "同意解封 QQ")
public class AgreeUnbanCommand extends QQExCommand {
    public static final String COMMAND_NAME = "同意解封";
    public AgreeUnbanCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return agreeUnban();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean agreeUnban() {
        if (argCheck(2)) {
            sendMsg("agree_unban.command.error.args_number");
            return false;
        }
        $7DTD._Log.info("同意解封");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            if (server.banUser.containsKey(args[1])) {
                if (args[1].equals(this.qq)) {
                    sendMsg("agree_unban.command.error.self");
                    return true;
                }
                List<String> count = server.banUser.get(args[1]);
                if (count.contains(this.qq)) {
                    sendMsg("agree_unban.command.error.agreed");
                    return true;
                }
                count.add(this.qq);
                server.banUser.put(args[1], count);
                if (count.size() >= server.serverData.unBanNum()) {
                    server.banUser.remove(args[1]);
                    //sendMsg("已满足解封所需人数，即将解除封禁");
                    UserConfig b = server.getUserData(args[1]);
                    if (server.kitNet.unBan(b.getSteamID())) sendAtMsg(args[1], "agree_unban.command.success");
                    else sendAtMsg(args[1], "agree_unban.command.error.net_error");
                } else sendMsg("agree_unban.command.error.agree");
            } else sendMsg("agree_unban.command.error.not_found");
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }

}
