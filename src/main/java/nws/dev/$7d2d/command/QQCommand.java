package nws.dev.$7d2d.command;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;

public abstract class QQCommand {
    protected final QQData.Message message;
    protected final String qq;
    protected final String group;
    protected final String msgId;
    public QQCommand(QQData.Message message) {
        this.message = message;
        this.qq = message.user_id;
        this.group = message.group_id;
        this.msgId = message.message_id;
    }
    public void sendGroupReplyMsg(String msg) {
        QQHelper.easySendGroupReplyMsg(this.group, msgId, msg);
    }

    public boolean check() {
        return false;
    }
    public boolean isAdmin() {
        return Config.I.getDatas().adminQQ.contains(this.qq);
    }
}
