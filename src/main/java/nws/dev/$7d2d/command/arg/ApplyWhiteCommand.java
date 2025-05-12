package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.ACItemsData;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = ApplyWhiteCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "申请白名单 白名单名称")
public class ApplyWhiteCommand extends QQExCommand {
    public static final String COMMAND_NAME = "申请白名单";
    public ApplyWhiteCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return applyWhite();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean applyWhite() {
        if (this.rawArg.isEmpty()) {
            sendMsg("apply_white.command.error.args_number");
            return false;
        }
        $7DTD._Log.info("申请白名单");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) sendMsg("usual.command.error.not_online");
            else {
                ACItemsData data = server.acItem.get(rawArg);
                if (data == null) data = server.autoWhiteList.getDatas().get(rawArg);
                if (data == null) sendMsg("apply_white.command.error.not_found");
                else {
                    if (data.allNeed() ? info.point() >= data.point() && info.level() >= data.level() : info.point() >= data.point() || info.level() >= data.level()) {
                        if (server.acNet.addWhite(info.userid(), data.getFormatItems())) sendMsg("apply_white.command.success");
                        else sendMsg("apply_white.command.error.net");
                    } else sendFormatMsg( "apply_white.command.error.not_met",  data.level() , data.point(), (data.allNeed() ? "需要等级和积分全部满足" : "需要等级或积分任一满足"));
                }
            }
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }

}
