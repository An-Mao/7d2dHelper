package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

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
