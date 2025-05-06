package nws.dev.$7d2d.command.at;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = "查看跟档物品",permission = Permission.ServerAdmin,type = CommandType.Group,desc = "查看跟档物品@QQ",priority = 990)
public class CheckSaveItemCommand extends QQAtCommand {
    public CheckSaveItemCommand(QQData.Message message, ServerCore serverCore) {
        super("checkSaveItem",message,serverCore);
    }

    @Override
    public boolean groupMsg() {
            if (this.target.isEmpty()) {
                sendMsg("指令格式错误，正确格式：查看跟档物品 @qq");
                return false;
            }else {
                UserConfig config = server.getUserData(this.target);
                if (config.isBind()) {
                    server.playerSaveItem.remove(this.qq);
                    server.playerSaveItemIndex.remove(this.qq);
                    UserConfig.RecordItem recordItem = config.getRecordItem(server);
                    if (recordItem.getDatas().isEmpty()) {
                        sendMsg("对方没有跟档物品");
                    }else {
                        StringBuilder s = new StringBuilder("对方跟档物品:");
                        int[] c = {0};
                        recordItem.getDatas().forEach((itemData) ->{
                            if (c[0] < 20) {
                                c[0]++;
                                s.append("\\n").append(OtherHelper.removeColorCodes(itemData.n())).append(" x ").append(itemData.c());
                            }
                        });
                        if (recordItem.getDatas().size() > 20){
                            server.playerSaveItem.put(this.qq,recordItem.getDatas());
                            server.playerSaveItemIndex.put(this.qq,20);
                            s.append("\\n...\\n发送【下一页】继续查看");
                        }
                        sendMsg(s.toString());
                    }
                } else sendMsg("对方未绑定账号");
            }
        return true;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
