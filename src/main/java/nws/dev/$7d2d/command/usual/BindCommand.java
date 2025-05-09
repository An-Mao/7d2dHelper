package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
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
        if (config.isBind()){
            $7DTD._Log.info("已绑定账号");
            QQHelper.sendGroupMsg(message.group_id,QQData.Msg.create(QQData.MsgType.Text,"text","您已绑定账号"));
        }else {
            $7DTD._Log.info("未绑定账号");
            String name = message.sender.card();
            if (name.isEmpty()) name = message.sender.nickname();
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerByName(name);
            if (info == null){
                $7DTD._Log.info("未找到玩家");
                sendMsg("未找到玩家，请确认您的昵称（"+name+"）与游戏昵称是否一致，注意大小写，并且是否在线");
            }else {
                config.setBind(info.userid());
                if (server.serverData.bindNeedGameMsg()){
                    server.bindUser.put(info.platformid(), this.qq);
                    sendMsg( "绑定已就绪，请在游戏内发送【绑定账号】来完成绑定。");
                }else {
                    config.setBindDone(info.platformid());
                    sendMsg( "绑定成功");
                }
            }
        }
        return true;
    }
}
