package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "领取礼包",permission = Permission.User,type = CommandType.Group,desc = "领取礼包 礼包名称")
public class ReceiveGiftCommand extends QQExCommand {
    public ReceiveGiftCommand(QQData.Message message, ServerCore serverCore) {
        super("receiveGift",message,serverCore);
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
            sendMsg("指令格式错误，正确格式：领取礼包 礼包名称");
            return false;
        }
        _Log.info("领取礼包");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            String r = args[1];
            _Log.debug("已绑定账号");
            if (config.isReward(r))
                sendMsg("您已领取过【" + r + "】礼包");
            else {
                sendMsg( server.botNet.giveReward(config, r));
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg("未绑定账号，请先绑定账号");
        }
        return true;
    }

}
