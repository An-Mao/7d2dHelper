package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandRegistryNew;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = AdminHelpCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.All)
public class AdminHelpCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "管理指令";

    public AdminHelpCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        Permission permission = Permission.getPermission(qq,server);
        if (permission.getPermission() > Permission.ServerAdmin.getPermission()) return false;
        StringBuilder stringBuilder = new StringBuilder("-----当前支持指令-----");
        CommandRegistryNew.getCommands().forEach((s, priorityQueue) -> {
            if (priorityQueue != null) {
                priorityQueue.forEach(commandInfo -> {
                    if (commandInfo.permission().getPermission() < Permission.User.getPermission() && permission.getPermission() <= commandInfo.permission().getPermission())
                        if (commandInfo.type() == CommandType.Group || commandInfo.type() == CommandType.All)
                            stringBuilder.append("\\n").append(commandInfo.desc());
                });
            }
        });
        stringBuilder.append(server.question).append("\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        sendMsg(stringBuilder.toString());
        return true;
    }

    @Override
    public boolean privateMsg() {
        Permission permission = Permission.getPermission(qq,server);
        if (permission.getPermission() > Permission.ServerAdmin.getPermission()) return false;
        StringBuilder stringBuilder = new StringBuilder("-----当前支持指令-----");
        CommandRegistryNew.getCommands().forEach((s, priorityQueue) -> {
            if (priorityQueue != null) {
                priorityQueue.forEach(commandInfo -> {
                    if (commandInfo.permission().getPermission() < Permission.User.getPermission() && permission.getPermission() <= commandInfo.permission().getPermission()  )
                        if (commandInfo.type() == CommandType.Private || commandInfo.type() == CommandType.All)
                            stringBuilder.append("\\n").append(commandInfo.desc());

                });
            }
        });
        stringBuilder.append("\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        $7DTD._Log.debug(stringBuilder.toString());
        sendMsg(stringBuilder.toString());
        return true;
    }
}
