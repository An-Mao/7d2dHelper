package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = KickSelfCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class KickSelfCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "踢自己";
    public KickSelfCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return kickSelf();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    public boolean kickSelf() {
        UserConfig config = server.getUserData(this.qq);
        if (!config.isBind()) {
            $7DTD._Log.debug("未绑定账号");
            sendMsg("未绑定账号，请先绑定账号");
            return true;
        }
        BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
        if (info == null) {
            $7DTD._Log.debug("未找到玩家");
            sendMsg("未找到玩家，请确认玩家是否在线");
        } else {
            if (server.kitNet.kick(config.getSteamID())) {
                $7DTD._Log.debug("强制下线成功");
                sendMsg("强制下线成功");
            } else
                sendMsg("强制下线失败，请稍后再试");
        }
        return true;

    }
}
