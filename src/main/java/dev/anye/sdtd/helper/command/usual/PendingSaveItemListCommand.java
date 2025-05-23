package dev.anye.sdtd.helper.command.usual;

import dev.anye.core.system._File;
import dev.anye.sdtd.helper.command.Command;
import dev.anye.sdtd.helper.command.CommandType;
import dev.anye.sdtd.helper.command.QQUsualCommand;
import dev.anye.sdtd.helper.config.UserConfig;
import dev.anye.sdtd.helper.data.Permission;
import dev.anye.sdtd.helper.data.QQData;
import dev.anye.sdtd.helper.server.ServerCore;

@Command(name = PendingSaveItemListCommand.COMMAND_NAME,permission = Permission.ServerAdmin,type = CommandType.Private)
public class PendingSaveItemListCommand extends QQUsualCommand {
    public static final String COMMAND_NAME = "待审核跟档列表";
    public PendingSaveItemListCommand(QQData.Message message, ServerCore serverCore) {
        super(COMMAND_NAME, message,serverCore);
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
