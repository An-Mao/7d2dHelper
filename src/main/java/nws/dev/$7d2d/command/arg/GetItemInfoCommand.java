package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.DataTable;
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

@Command(name = GetItemInfoCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查询物品 物品名称")
public class GetItemInfoCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查询物品";
    public GetItemInfoCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return getItemInfo();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean getItemInfo() {
        if (args[1].isEmpty()){
            sendMsg("find_item.command.error.empty");
            return false;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (s.isEmpty()) s.append(args[i]);
            else s.append(" ").append(args[i]);
        }
        $7DTD._Log.info("获取物品信息");

        String r = server.gameInfo.getItemInfo(s.toString());

        if (server.serverData.imageItem()){
            String md5 = DataTable.ImageCache+"/"+ _Byte.getMd5(r)+".png";
            if (!new File(md5).exists()) server.drawConfig.createImage(server.fontConfig,List.of(r.split("\\\\n"))).save(md5);

            QQHelper.sendMsg(Urls.qqSendGroupMsg,QQHelper.data.replace("<group>",this.group).replace("<file>",convertPathToUri(md5)));
        }else sendMsg(r);
        return true;
    }
}
