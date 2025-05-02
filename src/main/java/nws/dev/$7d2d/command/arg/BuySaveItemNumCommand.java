package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "购买跟档物品数量",permission = Permission.User,type = CommandType.Group)
public class BuySaveItemNumCommand extends QQExCommand {
    public BuySaveItemNumCommand(QQData.Message message, ServerCore serverCore) {
        super("buySaveItemNum", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return buySaveItemNum();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean buySaveItemNum() {
        if (argCheck(2)) {
            sendMsg("指令格式错误，正确格式：购买跟档物品数量 数量");
            return false;
        }
        _Log.info("提高跟档物品数量");
        UserConfig config = server.getUserData(this.qq);
        if (config.isBind()) {
            _Log.debug("已绑定账号");
            if (config.getRecordItemLimit() >= server.serverData.recordItemLimit()){
                _Log.debug("达到上限");
                sendMsg( "您的跟档物品数量已达到上限");
                return true;
            }
            BotData.PlayerInfo info = server.botNet.getOnlinePlayerBySteamID(config.getSteamID());
            if (info == null) {
                sendMsg("请在线后再试");
                return true;
            }
            int n = Integer.parseInt(args[1]);
            if (config.getRecordItemLimit() + n >= server.serverData.recordItemLimit()) n = server.serverData.recordItemLimit() - config.getRecordItemLimit();
            int p  = n * server.serverData.recordItemPoint();
            if (p <= 0) {
                sendMsg("数据错误，请重新尝试");
                return true;
            }
            if (info.point() < p){
                sendMsg("您的积分不足");
                return true;
            }
            if(server.botNet.sendPoint(config.getSteamID(), -p)){
                config.getDatas().recordItemLimit = config.getRecordItemLimit() + n;
                config.save();
                sendMsg( "已增加"+n+"个跟档物品数量，当前数量"+config.getRecordItemLimit());
            }else sendMsg("添加失败，请重新尝试");
        } else {
            _Log.debug("未绑定账号");
            sendMsg( "未绑定账号，请先绑定账号");
        }
        return true;
    }
}
