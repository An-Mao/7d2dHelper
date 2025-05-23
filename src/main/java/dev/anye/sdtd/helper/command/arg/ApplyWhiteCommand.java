package dev.anye.sdtd.helper.command.arg;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.ACItemsData;
import dev.anye.sdtd.helper.data.BotData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

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
                if (this.rawArg.contains(",")){
                    String[] ws = this.rawArg.split(",");
                    StringBuilder stringBuilder = new StringBuilder();
                    int success = 0;
                    for (String w : ws){
                        stringBuilder.append("[").append(w).append("]");
                        ACItemsData data = server.acItem.get(w);
                        if (data == null) data = server.autoWhiteList.getDatas().get(w);
                        if (data == null) {
                            stringBuilder.append(translate("apply_white.command.error.not_found"));
                        }
                        else {
                            if (data.allNeed() ? info.point() >= data.point() && info.level() >= data.level() : info.point() >= data.point() || info.level() >= data.level()) {
                                if (server.acNet.addWhite(info.userid(), data.getFormatItems())) {
                                    stringBuilder.append(translate("apply_white.command.success"));
                                    success++;
                                }
                                else stringBuilder.append(translate("apply_white.command.error.net"));
                            } else
                                stringBuilder.append(translate("apply_white.command.error.not_met", data.level(), data.point(), (data.allNeed() ? "需要等级和积分全部满足" : "需要等级或积分任一满足")));
                        }
                        stringBuilder.append("\\n");
                    }
                    sendFormatMsg("apply_white.command.done",success + "个白名单添加成功\\n",stringBuilder);
                    return true;
                }else {
                    ACItemsData data = server.acItem.get(rawArg);
                    if (data == null) data = server.autoWhiteList.getDatas().get(rawArg);
                    if (data == null) sendMsg("apply_white.command.error.not_found");
                    else {
                        if (data.allNeed() ? info.point() >= data.point() && info.level() >= data.level() : info.point() >= data.point() || info.level() >= data.level()) {
                            if (server.acNet.addWhite(info.userid(), data.getFormatItems()))
                                sendMsg("apply_white.command.success");
                            else sendMsg("apply_white.command.error.net");
                        } else
                            sendFormatMsg("apply_white.command.error.not_met", data.level(), data.point(), (data.allNeed() ? "需要等级和积分全部满足" : "需要等级或积分任一满足"));
                    }
                }
            }
        } else sendMsg("usual.command.error.not_bind");
        return true;
    }

}
