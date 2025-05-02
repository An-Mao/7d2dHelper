package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "领取新手礼包",permission = Permission.User,type = CommandType.Group)
public class GetNewPlayerGiftCommand extends QQUsualCommand {
    public GetNewPlayerGiftCommand(QQData.Message message, ServerCore serverCore) {
        super("getNewPlayerGift", message,serverCore);
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
        _Log.info("领取新手礼包");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()){
            _Log.debug("已绑定账号");
            if (config.isReward("NewbiePack"))
                sendMsg( "您已领取过新手礼包");
            else {
                sendMsg( server.botNet.giveReward(config,"NewbiePack"));
            }
        }else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
}
