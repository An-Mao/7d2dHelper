package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.*;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = HelpCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.All)
public class HelpCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "帮助";
    public HelpCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME,message,serverCore);
    }
    @Override
    public boolean runCommand() {
        if (c.isEmpty())return false;
        if (isGroup && isEnableGroup() && server.commandIsEnable(this.c))return groupMsg();
        if (!isGroup && server != null && server.commandIsEnable(this.c))return privateMsg();
        if (server == null) return privateMsg();
        return false;
    }



    @Override
    public boolean privateMsg() {
        $7DTD._Log.debug("获取帮助");
        Permission permission = Permission.getPermission(qq,server);
        StringBuilder stringBuilder = new StringBuilder("-----当前支持指令-----");
        CommandRegistryNew.getCommands().forEach((s, priorityQueue) -> {
            if (priorityQueue != null) {
                while (!priorityQueue.isEmpty()) {
                    CommandInfo commandInfo = priorityQueue.poll();
                    if (permission.getPermission() <= commandInfo.permission().getPermission())
                        if (commandInfo.type() == CommandType.Private || commandInfo.type() == CommandType.All)
                            stringBuilder.append("\\n").append(commandInfo.desc());

                }
            }
        });
        stringBuilder.append("\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        $7DTD._Log.debug(stringBuilder.toString());
        sendMsg(stringBuilder.toString());
        return true;
    }

    @Override
    public boolean groupMsg() {
        $7DTD._Log.debug("获取帮助");
        Permission permission = Permission.getPermission(qq,server);
        StringBuilder stringBuilder = new StringBuilder("-----当前支持指令-----");
        CommandRegistryNew.getCommands().forEach((s, priorityQueue) -> {
            if (priorityQueue != null) {
                while (!priorityQueue.isEmpty()) {
                    CommandInfo commandInfo = priorityQueue.poll();
                    if (permission.getPermission() <= commandInfo.permission().getPermission())
                        if (commandInfo.type() == CommandType.Group || commandInfo.type() == CommandType.All)
                            stringBuilder.append("\\n").append(commandInfo.desc());
                }
            }
        });
        stringBuilder.append(server.question).append("\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        sendMsg(stringBuilder.toString());
        return true;
    }
}
