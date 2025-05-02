package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.PlayerInfoData;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.OtherHelper;
import nws.dev.$7d2d.server.ServerCore;

import java.util.List;

@Command(name = "下一页",permission = Permission.ServerAdmin,type = CommandType.All)
public class NextPageCommand extends QQUsualCommand {
    public NextPageCommand(QQData.Message message, ServerCore serverCore) {
        super("nextPage", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return nextPage();
    }

    @Override
    public boolean privateMsg() {
        return nextPage();
    }

    private boolean nextPage() {
        if (server.playerSaveItem.containsKey(this.qq)) {
            List<PlayerInfoData.ItemData> itemDatas = server.playerSaveItem.get(this.qq);
            boolean has = true;
            int index = server.playerSaveItemIndex.get(this.qq);
            int maxIndex = index + 20;
            StringBuilder s = new StringBuilder("----第" + (index / 20 + 1) + "页----");
            for (int i = index; i < maxIndex; i++) {
                if (i >= itemDatas.size()) {
                    has = false;
                    break;
                }
                PlayerInfoData.ItemData itemData = itemDatas.get(i);
                s.append("\\n").append(OtherHelper.removeColorCodes(itemData.n())).append(" x ").append(itemData.c());
            }
            if (has) {
                server.playerSaveItemIndex.put(this.qq, maxIndex);
                s.append("\\n...\\n发送【下一页】继续查看");
            } else {
                server.playerSaveItem.remove(this.qq);
                server.playerSaveItemIndex.remove(this.qq);
                s.append("\\n---没有更多了---");
            }
            sendMsg(s.toString());
        }
        return true;
    }
}
