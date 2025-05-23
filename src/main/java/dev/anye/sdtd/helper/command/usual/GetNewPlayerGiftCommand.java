package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

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
