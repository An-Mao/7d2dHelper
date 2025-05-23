package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.data.RewardData;
import dev.anye.sdtd.helper.server.ServerCore;

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
                    if (server.botNet.sendPoint(userConfig.getSteamID(), -server.raffleConfig.getDatas().point)){
                        $7DTD._Log.debug(s.name);
                        sendFormatMsg("raffle.command.success", server.raffleConfig.getDatas().point , server.botNet.giveReward(userConfig, s));
                    }else sendMsg("usual.command.error.point.sub");
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
