package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandRegistryNew;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = AdminHelpCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.All)
public class AdminHelpCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "管理指令";

    public AdminHelpCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        Permission permission = Permission.getPermission(qq,server);
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
        //if (permission.getPermission() > Permission.ServerAdmin.getPermission()) return false;
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
