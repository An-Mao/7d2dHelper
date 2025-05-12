package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.BotData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.data.RewardData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = RaffleCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class RaffleCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "抽奖";
    public RaffleCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        return raffle();
    }
    private boolean raffle() {
        if (!server.raffleConfig.getDatas().enable){
            return false;
        }
        UserConfig userConfig = server.getUserData(this.qq);
        if (userConfig.isBind()) {
            if (System.currentTimeMillis() - userConfig.getDatas().raffleTime < server.raffleConfig.getDatas().cooldown * 1000L) {
                sendMsg("raffle.command.error.cooldown");
                return true;
            }
            BotData.PlayerInfo playerInfo = server.botNet.getOnlinePlayerBySteamID(userConfig.getSteamID());
            if (playerInfo == null) sendMsg("usual.command.error.not_online");
            else {
                if (playerInfo.point() < server.raffleConfig.getDatas().point) {
                    sendMsg("usual.command.error.point_insufficient");
                    return true;
                }
                RewardData s = server.raffleConfig.getDatas().random().getRandom();
                if (s != null) {
                    userConfig.getDatas().raffleTime = System.currentTimeMillis();
                    userConfig.save();

                    server.botNet.addPoint(userConfig, -server.raffleConfig.getDatas().point);
                    $7DTD._Log.debug(s.name);

                    sendFormatMsg("raffle.command.success", server.raffleConfig.getDatas().point , server.botNet.giveReward(userConfig, s));
                }else sendMsg("raffle.command.error.net");
            }
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
