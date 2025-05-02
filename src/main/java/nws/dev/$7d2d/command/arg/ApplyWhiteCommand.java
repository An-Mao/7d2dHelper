package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.ACItemsData;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "申请白名单",permission = Permission.User,type = CommandType.Group)
public class ApplyWhiteCommand extends QQExCommand {
    public ApplyWhiteCommand(QQData.Message message, ServerCore serverCore) {
        super("applyWhite", message,serverCore);
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
            sendMsg("指令格式错误，正确格式：申请白名单 白名单名称");
            return false;
        }
        _Log.info("申请白名单");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            _Log.debug("已绑定账号");
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                _Log.debug("未找到玩家");
                sendMsg( "未找到玩家，请确认玩家是否在线");
            } else {
                ACItemsData data = server.acItem.get(rawArg);
                if (data == null) data = server.autoWhiteList.getDatas().get(rawArg);
                if (data == null) {
                    _Log.debug("未找到此白名单");
                    sendMsg( "未找到此白名单，请确认此白名单是否存在");
                } else {
                    if (data.allNeed() ? info.point() >= data.point() && info.level() >= data.level() : info.point() >= data.point() || info.level() >= data.level()) {
                        _Log.debug("白名单检测成功");
                        if (server.acNet.addWhite(info.userid(), data.getFormatItems())) {
                            sendMsg( "白名单添加成功");
                        } else {
                            sendMsg( "白名单添加失败，网络异常");
                        }
                    } else {
                        sendMsg( "白名单添加失败，你未满足白名单要求：" + "\\n需要等级：" + data.level() + "\\n需要积分：" + data.point() + "\\n" + (data.allNeed() ? "需要等级和积分全部满足" : "需要等级或积分任一满足"));

                    }

                }
            }
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }

}
