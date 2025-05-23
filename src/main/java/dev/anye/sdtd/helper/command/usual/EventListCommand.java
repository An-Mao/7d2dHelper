package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = EventListCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group)
public class EventListCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "活动列表";
    public EventListCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
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
        $7DTD._Log.info("获取活动列表");
        StringBuilder s = new StringBuilder();
        server.eventList.getDatas().forEach((k, v) -> s.append("\\n[").append(k).append("]:").append(v));

        $7DTD._Log.debug(s.toString());
        sendFormatMsg("event_list.command.tip",s.toString());
        return true;

    }
}
