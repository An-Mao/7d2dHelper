package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.KitData;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

import java.time.LocalTime;

@Command(name = ServerInfoCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class ServerInfoCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "服务器信息";
    public ServerInfoCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return serverInfo();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean serverInfo() {
        $7DTD._Log.info("获取服务器状态");
        sendMsg(getServerInfo());
        return true;
    }

    public String getServerInfo(){
        KitData.GsList list = server.kitNet.getGsList();
        if (list.result() == 1) {
            StringBuilder s = new StringBuilder("===服务器信息===");
            for (KitData.GsInfo info : list.list()) {
                s.append("\\n运行状态：").append(getServerStatus(info.status()));
                s.append("\\n当前帧率：").append(info.fps());
                s.append("\\n在线人数：").append(info.players());
                s.append("\\n重启时间：").append(LocalTime.ofSecondOfDay(info.time()));
            }
            s.append("\\n").append(server.botNet.getGameState().toString());
            return s.toString();
        }
        return "获取服务器信息失败";
    }

    public String getServerStatus(int status){
        return switch (status){
            case 1 -> "运行中";
            case 2 -> "重启中";
            case 3 -> "已关闭";
            default -> "未知";
        };
    }
}
