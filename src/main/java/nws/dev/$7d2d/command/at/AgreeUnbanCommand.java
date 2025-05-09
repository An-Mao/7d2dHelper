package nws.dev.$7d2d.command.at;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
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
            $7DTD._Log.debug("已绑定账号");

            if (server.banUser.containsKey(target)) {
                if (target.equals(this.qq)) {
                    $7DTD._Log.debug("相同玩家");
                    sendMsg( "您不能同意自己的解封申请");
                    return true;
                }
                List<String> count = server.banUser.get(target);
                if (count.contains(this.qq)) {
                    $7DTD._Log.debug("已同意过此玩家的解封申请");
                    sendMsg( "您已同意过此玩家的解封申请");
                    return true;
                }
                count.add(this.qq);
                server.banUser.put(target, count);
                if (count.size() >= server.serverData.unBanNum()) {
                    server.banUser.remove(target);
                    sendMsg( "已满足解封所需人数，即将解除封禁");
                    UserConfig b = server.getUserData(target);
                    if (server.kitNet.unBan(b.getSteamID())) {
                        QQHelper.easySendGroupAtMsg(message.group_id, target, "解封成功");
                    } else {
                        QQHelper.easySendGroupAtMsg(message.group_id, target, "解封失败，网络异常");
                    }
                } else {
                    $7DTD._Log.debug("未满足解封所需人数");
                    sendMsg( "您已同意此玩家的解封申请");
                }
            } else {
                $7DTD._Log.debug("未找到此玩家");
                sendMsg( "未找到此封禁玩家");
            }
        } else {
            $7DTD._Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
}
