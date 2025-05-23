package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.KitData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.helper.OtherHelper;
import dev.anye.sdtd.helper.server.ServerCore;

import java.util.ArrayList;

@Command(name = RequestUnbanCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class RequestUnbanCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "申请解封";
    public RequestUnbanCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return requestUnban();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    public boolean requestUnban() {
        $7DTD._Log.info("申请解封");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            if (server.banUser.containsKey(this.qq)){
                sendFormatMsg( "request_unban.command.error.has",server.banUser.get(this.qq).size());
                return true;
            }
            KitData.BanUser ban = server.kitNet.getBan(config.getSteamID());
            if (ban != null){
                String s = "查询到您的封禁记录\\n";
                s += "封禁原因："+ OtherHelper.removeColorCodes(ban.banreason())+"\\n";
                s += server.acNet.getLog(config.getUserId()) +"\\n";
                s += "如果你想解封需要"+ server.serverData.unBanNum()+"个已绑定账号的群员发送【同意解封 "+this.qq+"】来解封。";
                server.banUser.put(this.qq,new ArrayList<>());
                sendMsg( s);
            }else sendMsg( "request_unban.command.error.not_found");
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }
}
