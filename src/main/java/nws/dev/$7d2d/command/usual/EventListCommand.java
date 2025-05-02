package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "活动列表",permission = Permission.User,type = CommandType.Group)
public class EventListCommand extends QQUsualCommand {
    public EventListCommand(QQData.Message message, ServerCore serverCore) {
        super("eventList", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return eventList();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean eventList() {
        _Log.info("获取活动列表");
        StringBuilder s = new StringBuilder("-----活动列表-----");
        server.eventList.getDatas().forEach((k, v) -> s.append("\\n[").append(k).append("]:").append(v));
        sendMsg(s.toString());
        return true;

    }
}
