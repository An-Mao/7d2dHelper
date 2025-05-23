package dev.anye.sdtd.helper.command.arg;

import dev.anye.core.bytes._Byte;
import dev.anye.sdtd.helper.$7DTD;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQExCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.helper.QQHelper;
import dev.anye.sdtd.helper.net.Urls;
import dev.anye.sdtd.helper.server.ServerCore;

import java.io.File;
import java.util.List;

@Command(name = RequestRecipeCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查询配方 物品名称")
public class RequestRecipeCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查询配方";
    public RequestRecipeCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return requestRecipe();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean requestRecipe() {
        if (args[1].isEmpty()){
            sendMsg("find_item.command.error.empty");
            return false;
        }
        $7DTD._Log.info("查询配方");
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (s.isEmpty()) s.append(args[i]);
            else s.append(" ").append(args[i]);
        }
        String r = server.gameInfo.getRecipeInfo(s.toString());
        if (server.serverData.imageRecipes()){
            String md5 = server.imageCache+"/"+ _Byte.getMd5(r)+".png";
            if (!new File(md5).exists()) server.drawConfig.createImage(server.fontConfig,List.of(r.split("\\\\n"))).save(md5);
            QQHelper.sendMsg(Urls.qqSendGroupMsg,QQHelper.data.replace("<group>",this.group).replace("<file>",convertPathToUri(md5)));
            //QQHelper.sendGroupMsg(this.group,QQData.Msg.create(QQData.MsgType.Image, "file",convertPath(md5)));
        }else {
            sendMsg(r);
        }
        return true;
    }
}
