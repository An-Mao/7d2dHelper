package nws.dev.$7d2d.command.usual;

import nws.dev.$7d2d.command.Command;
import nws.dev.$7d2d.command.CommandType;
import nws.dev.$7d2d.command.QQUsualCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.Permission;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;
import nws.dev.$7d2d.system._File;

@Command(name = "待审核跟档列表",permission = Permission.ServerAdmin,type = CommandType.Private)
public class PendingSaveItemListCommand extends QQUsualCommand {
    public PendingSaveItemListCommand(QQData.Message message, ServerCore serverCore) {
        super("pendingSaveItemList", message,serverCore);
    }

    @Override
    public boolean groupMsg() {
        return false;
    }

    @Override
    public boolean privateMsg() {
        return pendingSaveItemList();
    }

    private boolean pendingSaveItemList() {
        if (isAdmin()) {
            StringBuilder sb = new StringBuilder("------------");
            _File.getFiles(server.severUserDataDir, ".json").forEach(p->{
                String uqq = p.getFileName().toString().replace(".json","");
                UserConfig config = server.getUserData(server.severUserDataDir+"/"+ uqq+".json");

                if (!config.getDatas().canExtractSaveItem && !config.getRecordItem(server).getDatas().isEmpty()){
                    sb.append("\\n【").append(uqq).append("】 ").append(config.getRecordItem(server).getDatas().size());
                }
            });
            sendMsg(sb.toString());
        }
        return true;
    }
}
