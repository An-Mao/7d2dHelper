package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.KitData;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

import java.time.LocalTime;

@Command(name = "服务器信息",permission = Permission.User,type = CommandType.Group)
public class ServerInfoCommand extends QQUsualCommand {
    public ServerInfoCommand(QQData.Message message, ServerCore serverCore) {
        super("serverInfo", message,serverCore);
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
        _Log.info("获取服务器状态");
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
