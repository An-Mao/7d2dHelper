package dev.anye.sdtd.helper.command.usual;

import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.PlayerInfoData;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.helper.OtherHelper;
import dev.anye.sdtd.helper.server.ServerCore;

import java.util.List;

@Command(name = NextPageCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.All)
public class NextPageCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "下一页";
    public NextPageCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
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
