package nws.dev.$7d2d.command.arg;

import nws.dev.$7d2d.$7DTD;
import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQExCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

@Command(name = FindWhiteCommand.COMMAND_NAME,permission = Permission.User,type = CommandType.Group,desc = "查找白名单 物品名称")
public class FindWhiteCommand extends QQExCommand {
    public static final String COMMAND_NAME = "查找白名单";
    public FindWhiteCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return findWhite();
    }

    @Override
    public boolean privateMsg() {
        return false;
    }

    private boolean findWhite() {
        if (rawArg.isEmpty()){
            sendMsg("物品名称不能为空");
            return false;
        }
        $7DTD._Log.info("查找白名单");
        StringBuilder s = new StringBuilder();
        int[] c = {0};
        server.acItem.getDatas().forEach((k, v) -> {
            for(String item : v.items()){
                /*
                if (item.contains(this.args[1])) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }

                 */
                boolean full = true;
                for (int i = 1; i < this.args.length; i++) {
                    if (!item.contains(this.args[i])) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }
            }
        });
        server.autoWhiteList.getDatas().forEach((k, v) -> {
            for(String item : v.items()){
                boolean full = true;
                for (int i = 1; i < this.args.length; i++) {
                    if (!item.contains(this.args[i])) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    c[0]++;
                    s.append("\\n").append(k);
                    break;
                }
            }
        });

        if (c[0] > 20) {
            sendMsg("结果数量过多，请提供更多字词并重新查找");
            return true;
        }
        if (s.isEmpty()) sendMsg("未找到包含此物品的白名单，请检查是否有错误。");
        else sendMsg("包含类似物品的白名单："+ s);
        return true;
    }
}
