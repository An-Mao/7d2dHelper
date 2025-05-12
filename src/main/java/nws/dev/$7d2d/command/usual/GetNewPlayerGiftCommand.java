package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = GetNewPlayerGiftCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class GetNewPlayerGiftCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "领取新手礼包";
    public GetNewPlayerGiftCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return getNewPlayerGift();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean getNewPlayerGift() {
        $7DTD._Log.info("领取新手礼包");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()){
            if (config.isReward("NewbiePack")) sendMsg("get_new_player_gift.command.error.has");
            else sendMsg(server.botNet.giveReward(config,"NewbiePack"));
        }else sendMsg("usual.command.error.not_bind");
        return true;
    }
}
