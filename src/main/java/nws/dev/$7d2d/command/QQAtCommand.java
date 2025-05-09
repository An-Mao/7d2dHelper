package nws.dev.$7d2d.command;

import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.server.ServerCore;

public abstract class QQAtCommand extends QQCommand {
    protected final String command;
    protected final String target;
    public QQAtCommand(String c, QQData.Message message, ServerCore serverCore) {
        super(c,message,serverCore);
        if (message.message.size() == 2) {
            QQData.Message.Msg msg = message.message.get(0);
            if (msg.type().equals("text") && msg.data().containsKey("text") ) {
                this.command = msg.data().get("text");
                msg = message.message.get(1);
                if (msg.type().equals("at") && msg.data().containsKey("qq")) {
                    this.target = msg.data().get("qq");
                }else this.target = "";
            }else {
                this.command = "";
                this.target = "";
            }
        }else {
            this.command = "";
            this.target = "";
        }
    }

    @Override
    public boolean runCommand() {
        return super.runCommand() && !this.target.isEmpty();
    }
}
