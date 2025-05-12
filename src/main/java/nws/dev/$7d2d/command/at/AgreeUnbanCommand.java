package nws.dev.$7d2d.command.at;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

import java.util.List;

@Command(name = AgreeUnbanCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "同意解封@QQ",priority = 990)
public class AgreeUnbanCommand extends QQAtCommand {
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
        if (target.isEmpty()) {
            //sendMsg("指令格式错误，正确格式：同意解封@qq");
            return false;
        }
        $7DTD._Log.info("同意解封");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            if (server.banUser.containsKey(target)) {
                if (target.equals(this.qq)) {
                    sendMsg( "agree_unban.command.error.self");
                    return true;
                }
                List<String> count = server.banUser.get(target);
                if (count.contains(this.qq)) {
                    sendMsg( "agree_unban.command.error.agreed");
                    return true;
                }
                count.add(this.qq);
                server.banUser.put(target, count);
                if (count.size() >= server.serverData.unBanNum()) {
                    server.banUser.remove(target);
                    UserConfig b = server.getUserData(target);
                    if (server.kitNet.unBan(b.getSteamID())) sendAtMsg(target, "agree_unban.command.success");
                    else sendAtMsg(target, "agree_unban.command.error.net_error");
                } else sendMsg("agree_unban.command.error.agree");
            } else sendMsg("agree_unban.command.error.not_found");
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }
}
