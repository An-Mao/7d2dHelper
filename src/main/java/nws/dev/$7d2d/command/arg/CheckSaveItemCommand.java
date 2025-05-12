package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = CheckSaveItemCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Private,desc = "查看跟档物品 QQ")
public class CheckSaveItemCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查看跟档物品";
    public CheckSaveItemCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        return checkSaveItem();
    }

    private boolean checkSaveItem() {
        if (isAdmin()) {
            String target = args[1];
            if (target.isEmpty()) {
                sendMsg("check_save_item.command.error.args_number");
                return false;
            }else {
                UserConfig config = server.getUserData(target);
                if (config.isBind()) {
                    server.playerSaveItem.remove(this.qq);
                    server.playerSaveItemIndex.remove(this.qq);
                    UserConfig.RecordItem recordItem = config.getRecordItem(server);
                    if (recordItem.getDatas().isEmpty()) sendMsg("check_save_item.command.error.target_not_have_item");
                    else {
                        StringBuilder s = new StringBuilder();
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
                        sendFormatMsg("check_save_item.command.target_items",s.toString());
                    }
                } else sendMsg("usual.command.error.target_not_bind");
            }
        }
        return true;
    }
}
