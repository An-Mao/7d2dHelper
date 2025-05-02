package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandRegistryNew;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Log;

@Command(name = "帮助",permission = Permission.User,type = CommandType.All)
public class HelpCommand extends QQUsualCommand {
    public HelpCommand(QQData.Message message, ServerCore serverCore) {
        super("help",message,serverCore);
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
        _Log.debug("获取帮助");
        Permission permission = Permission.getPermission(qq,server);
        StringBuilder stringBuilder = new StringBuilder("-----当前支持指令-----");
        CommandRegistryNew.getCommandMap().forEach((s, commandInfo) -> {
            if (permission.getPermission() <= commandInfo.permission().getPermission())
                if (commandInfo.type() == CommandType.Private || commandInfo.type() == CommandType.All) stringBuilder.append("\\n").append(s);

        });
        stringBuilder.append("\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        _Log.debug(stringBuilder.toString());
        sendMsg(stringBuilder.toString());
        return true;
    }

    @Override
    public boolean groupMsg() {
        _Log.debug("获取帮助");
        Permission permission = Permission.getPermission(qq,server);
        StringBuilder stringBuilder = new StringBuilder("-----当前支持指令-----");
        CommandRegistryNew.getCommandMap().forEach((s, commandInfo) -> {
            if (permission.getPermission() <= commandInfo.permission().getPermission())
                if (commandInfo.type() == CommandType.Group || commandInfo.type() == CommandType.All) stringBuilder.append("\\n").append(s);

        });
        stringBuilder.append(server.question).append("\\n=============\\n【】为必须参数，（）为可选参数，用空格隔开。");
        sendMsg(stringBuilder.toString());
        return true;
    }
}
