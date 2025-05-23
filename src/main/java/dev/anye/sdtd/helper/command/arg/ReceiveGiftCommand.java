package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = ReceiveGiftCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "领取礼包 礼包名称")
public class ReceiveGiftCommand extends QQExCommand {
    public static final String COMMAND_NAME = "领取礼包";
    public ReceiveGiftCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME,message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return receiveGift();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean receiveGift() {
        if (argCheck(2)) {
            sendMsg("receive_gift.command.error.args_number");
            return false;
        }
        $7DTD._Log.info("领取礼包");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            String r = args[1];
            if (config.isReward(r)) sendFormatMsg("receive_gift.command.error.has", r );
            else sendMsg( server.botNet.giveReward(config, r));
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }

}
