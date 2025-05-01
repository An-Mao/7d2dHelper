package nws.dev.$7d2d.command;

import nws.dev.$7d2d.config.Config;
import nws.dev.$7d2d.data.QQData;
import nws.dev.$7d2d.helper.QQHelper;
import nws.dev.$7d2d.system._Log;

public abstract class QQCommand {
    protected final QQData.Message message;
    protected final String qq;
    protected final String group;
    protected final String msgId;
    protected final boolean isGroup;
    protected final String msg;
    public QQCommand(QQData.Message message) {
        this.message = message;
        this.qq = message.user_id;
        this.isGroup = !message.message_type.equals("private");
        this.group = message.group_id;
        this.msgId = message.message_id;
        //_Log.debug("qq:" + this.qq + " group:" + this.group + " isGroup:" + this.isGroup);
        if (this.message.message.size() == 1 && this.message.message.get(0).type().equals("text")) {
            this.msg = this.message.message.get(0).data().get("text");
        }else {
            this.msg = "";
        }
    }
    public void sendMsg(String msg) {
        if (isGroup && isEnableGroup()) {
            _Log.debug("发送群聊消息");
            QQHelper.easySendGroupReplyMsg(this.group, msgId, msg);
        }else {
            _Log.debug("发送私聊消息");
            QQHelper.easySendReplyMsg(this.qq, msgId, msg);
        }
    }

    public boolean check() {
        if (isGroup && isEnableGroup())return groupMsg();
        return privateMsg();
    }

    public boolean isEnableGroup() {
        return Config.I.getDatas().qqGroup.contains(message.group_id);
    }


    public boolean groupMsg(){
        return false;
    }
    public boolean privateMsg(){
        return false;
    }


    public boolean isAdmin() {

        return Config.I.getDatas().adminQQ.contains(this.qq);
    }
}
