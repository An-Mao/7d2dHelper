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

@Command(name = BindCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class BindCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "绑定账号";
    public BindCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return bind();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean bind(){
        $7DTD._Log.info("绑定账号");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) sendMsg("usual.command.error.bind");
        else {
            String name = message.sender.card();
            if (name.isEmpty()) name = message.sender.nickname();
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerByName(name);
            if (info == null) sendFormatMsg("bind.command.error.not_found",name);
            else {
                config.setBind(info.userid());
                if (server.serverData.bindNeedGameMsg()){
                    server.bindUser.put(info.platformid(), this.qq);
                    sendMsg( "bind.command.success.step.1");
                }else {
                    config.setBindDone(info.platformid());
                    sendMsg( "bind.command.success.step.done");
                }
            }
        }
        return true;
    }
}
