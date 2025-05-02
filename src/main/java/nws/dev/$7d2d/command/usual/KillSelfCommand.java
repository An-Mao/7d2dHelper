package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "自杀",permission = Permission.User,type = CommandType.Group)
public class KillSelfCommand extends QQUsualCommand {
    public KillSelfCommand( QQData.Message message, ServerCore serverCore) {
        super("killSelf", message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return killSelf();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean killSelf() {
        _Log.info("自杀");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                _Log.debug("未找到玩家");
                sendMsg( "未找到玩家，请确认玩家是否在线");
            }else {
                if (server.sendServerCommand("kill "+info.entityid())) {
                    _Log.debug("自杀成功");
                    sendMsg( "自杀成功");
                } else
                    sendMsg( "自杀失败，请稍后再试");
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
}
