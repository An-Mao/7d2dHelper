package nws.dev.$7d2d.command.at;

import nws.dev.$7d2d.command.QQAtCommand;
import nws.dev.$7d2d.config.UserConfig;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

public class RequestInfoAtCommand extends QQAtCommand {
    public RequestInfoAtCommand(String c, QQData.Message message, ServerCore serverCore) {
        super(c, message, serverCore);
    }

    @Override
    public boolean groupMsg() {
        if (this.target.isEmpty()) return false;
        UserConfig config = server.getUserData(this.target);
        if (config.isBind()) {
            sendMsg("");
        }
        return false;
    }

    @Override
    public boolean privateMsg() {
        return false;
    }
}
