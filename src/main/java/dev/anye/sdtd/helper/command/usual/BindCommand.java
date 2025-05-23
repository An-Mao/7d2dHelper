package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

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
