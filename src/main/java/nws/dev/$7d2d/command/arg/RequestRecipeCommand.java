package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.Urls;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.core.bytes._Byte;

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
            sendMsg("物品名称不能为空");
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
            /*
            HashMap<String,String> map = new HashMap<>();
            map.put("file",convertPath(md5));
            map.put("subType","0");
            map.put("id","40000");
            _Log.debug(r);

             */

            if (!new File(md5).exists()) server.drawConfig.createImage(server.fontConfig,List.of(r.split("\\\\n"))).save(md5);
            QQHelper.sendMsg(Urls.qqSendGroupMsg,QQHelper.data.replace("<group>",this.group).replace("<file>",convertPathToUri(md5)));
            //QQHelper.sendGroupMsg(this.group,QQData.Msg.create(QQData.MsgType.Image, "file",convertPath(md5)));
        }else {
            sendMsg(r);
        }
        return true;
    }
}
