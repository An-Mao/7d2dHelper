package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.DataTable;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.net.Urls;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._Byte;
import nws.dev.$7d2d.system._Log;

import java.io.File;
import java.util.List;

@Command(name = "查询物品",permission = Permission.User,type = CommandType.Group)
public class GetItemInfoCommand extends QQExCommand {
    public GetItemInfoCommand(QQData.Message message, ServerCore serverCore) {
        super("getItemInfo", message,serverCore);
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
            sendMsg("物品名称不能为空");
            return false;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (s.isEmpty()) s.append(args[i]);
            else s.append(" ").append(args[i]);
        }
        _Log.info("获取物品信息");

        String r = server.gameInfo.getItemInfo(s.toString());

        if (server.serverData.imageItem()){
            String md5 = DataTable.ImageCache+"/"+ _Byte.getMD5(r)+".png";
            if (!new File(md5).exists()) server.drawConfig.createImage(server.fontConfig,List.of(r.split("\\\\n"))).save(md5);

            QQHelper.sendMsg(Urls.qqSendGroupMsg,QQHelper.data.replace("<group>",this.group).replace("<file>",convertPathToUri(md5)));
        }else {
            sendMsg(r);
        }
        return true;
    }
}
